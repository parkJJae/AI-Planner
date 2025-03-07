package com.example.aiplanner.service;

import com.example.aiplanner.model.planner.PlannerEntity;
import com.example.aiplanner.model.planner.PlannerStatus;
import com.example.aiplanner.model.planner.TaskEntity;
import com.example.aiplanner.model.user.UserEntity;
import com.example.aiplanner.repository.PlannerRepository;
import com.example.aiplanner.repository.TaskRepository;
import com.example.aiplanner.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIPlannerService {

    private final RestTemplate restTemplate; // HTTP 요청을 보내기 위함
    private final PlannerRepository plannerRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent?key=AIzaSyAZ-Y4dN9GriecS7zs4lLyRas93eDmuLoA";

    // Gemini AI에 보낼 요청 데이터를 JSON 형식으로 만드는 메서드
    private String createAIRequestBody(List<String> tasks) {
       // 할 일 목록을 쉼표로 구분된 하나의 문자열로 예: "운동하기, 공부하기, 요리하기"
        String taskListString = tasks.stream()
                .collect(Collectors.joining(", "));

        return "{ " +
                "\"contents\": [{" +
                "\"parts\": [{" +
                "\"text\": \"다음 할 일들을 기반으로 24시간 각 일정을 만들어줘(예: 07:00시 기상): " + taskListString + "\"" +
                "}]" +
                "}], " +
                "\"generationConfig\": {" +
                "\"temperature\": 0.9," +
                "\"topK\": 1," +
                "\"topP\": 1," +
                "\"maxOutputTokens\": 2048," +
                "\"stopSequences\": []" +
                "}," +
                "\"safetySettings\": [{" +
                "\"category\": \"HARM_CATEGORY_HARASSMENT\"," +
                "\"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"" +
                "}, {" +
                "\"category\": \"HARM_CATEGORY_HATE_SPEECH\"," +
                "\"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"" +
                "}, {" +
                "\"category\": \"HARM_CATEGORY_SEXUALLY_EXPLICIT\"," +
                "\"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"" +
                "}, {" +
                "\"category\": \"HARM_CATEGORY_DANGEROUS_CONTENT\"," +
                "\"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"" +
                "}]" +
                "}";
    }

   // Gemini에 요청을 보내고, Gemini의 응답을 반환
    private String callAIService(String requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); //이게 없으면 서버가 요청을 JSON 형식으로 인식못함
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(GEMINI_API_URL, entity, String.class); // Post요청을 보내고 응답을 받아옴
            return response;
        } catch (Exception e) {
            System.err.println("AI 서비스 호출 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("AI 서비스 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * AI 응답을 파싱하여 PlannerEntity 및 TaskEntity를 생성하는 메서드
     */
    private PlannerEntity savePlanner(UserEntity user, String aiSchedule, List<String> inputTasks) {
        PlannerEntity planner = new PlannerEntity();
        planner.setUser(user);
        planner.setCreatedAt(LocalDateTime.now());
        //planner.setUpdatedAt(LocalDateTime.now());
        planner.setPlannerTitle(LocalDate.now()+"일 일정");
        planner.setAiGenerated(true);
        //planner.setCompleted(false);
        //planner.setDeleted(false);
        //planner.setStatus(PlannerStatus.PENDING);

        // 플래너 저장 (초기 단계)
        PlannerEntity savedPlanner = plannerRepository.save(planner);

        // AI 응답을 파싱해서 TaskEntity 목록 생성 (GEMINI 응답을 TaskEntity에 저장하기 위함)
        List<TaskEntity> taskEntities = parseAndCreateTasks(savedPlanner, aiSchedule);
        // 만약 파싱 결과가 없으면, 입력받은 할 일 목록을 폴백으로 사용
        if (taskEntities.isEmpty()) {
            taskEntities = inputTasks.stream()
                    .map(task -> {
                        TaskEntity t = new TaskEntity();
                        t.setPlanner(savedPlanner);
                        t.setTaskName(task);
                       // t.setCompleted(false);
                        return t;
                    })
                    .collect(Collectors.toList());
        }

        // TaskEntity 저장
        taskRepository.saveAll(taskEntities);
        // PlannerEntity의 tasks 필드에 할 일 목록 설정
        savedPlanner.setTasks(taskEntities);

        return savedPlanner;
    }

    // Json을 분석하여 TaskEntity에 저장하는 매서드
    private List<TaskEntity> parseAndCreateTasks(PlannerEntity planner, String aiSchedule) {
        List<TaskEntity> taskEntities = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiSchedule); // Tree형식으로
            JsonNode candidates = rootNode.path("candidates"); // GEMINI JSON응답에 첫 번째 candidates포함
            if (candidates.isArray() && candidates.size() > 0) {
                // 첫 번째 후보의 content.parts[0].text 추출
                String generatedText = candidates.get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();

                String[] lines = generatedText.split("\n");
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("*") || line.startsWith("-")) { // 보통 AI 응답에서 List형식으로 되면 보통 *또는 -을 처음 시작에 사용한다고 함
                       String taskTitle = line.substring(1).trim();
                        if (!taskTitle.isEmpty()) {
                            TaskEntity task = new TaskEntity();
                            task.setPlanner(planner);
                            task.setTaskName(taskTitle);
                           // task.setCompleted(false);
                            taskEntities.add(task);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("AI 응답 파싱 중 오류 발생: " + e.getMessage());
        }
        return taskEntities;
    }

    public PlannerEntity generateAIPlanner(Long userId, List<String> tasks) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // AI에 요청할 데이터 생성
        String requestBody = createAIRequestBody(tasks);

        // Gemini API 호출
        String aiGeneratedSchedule = callAIService(requestBody);

        // AI가 생성한 일정 파싱 후 저장 (할 일 목록 포함)
        PlannerEntity planner = savePlanner(user, aiGeneratedSchedule, tasks);

        return planner;
    }
}
