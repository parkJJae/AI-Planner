package com.example.aiplanner.service;

import com.example.aiplanner.model.planner.TaskEntity;
import com.example.aiplanner.model.planner.TaskRequestDto;
import com.example.aiplanner.model.planner.TaskResponseDto;
import com.example.aiplanner.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Data
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * 사용자가 입력한 TaskRequestDto를 받아서 TaskEntity로 변환한 후,
     * 무료 Gemini API를 호출하여 AI가 추천한 시작 시간과 종료 시간을 설정하고 저장합니다.
     * 최종적으로 TaskResponseDto로 변환하여 반환합니다.
     */
    @Transactional
    public TaskResponseDto addTask(TaskRequestDto requestDto) {
        // 1. 사용자가 입력한 할 일(taskName)을 기반으로 새로운 TaskEntity 생성
        TaskEntity task = new TaskEntity();
        task.setTaskName(requestDto.getTaskName());
        task.setCompleted(false);

        // 2. 무료 Gemini API를 호출하여 추천 시작 시간과 종료 시간 받기
        LocalTime[] recommendedTimes = callGeminiApi(task.getTaskName());
        task.setStartTime(recommendedTimes[0]);
        task.setEndTime(recommendedTimes[1]);

        // 3. TaskEntity를 DB에 저장
        TaskEntity savedTask = taskRepository.save(task);

        // 4. 저장된 TaskEntity를 TaskResponseDto로 변환하여 반환
        return new TaskResponseDto(savedTask);
    }

    /**
     * 무료 Gemini API를 호출하여 할 일(taskName)에 대한 추천 시작 시간과 종료 시간을 가져옵니다.
     * 만약 API 호출이 실패하면 기본값을 반환합니다.
     *
     * @param taskName 사용자가 입력한 할 일 이름
     * @return LocalTime 배열 {startTime, endTime}
     */
    private LocalTime[] callGeminiApi(String taskName) {
        // RestTemplate을 사용하여 HTTP 요청을 보냅니다.
        RestTemplate restTemplate = new RestTemplate();

        // 요청 바디 생성: 할 일 이름을 포함하는 Map
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("taskName", taskName);

        // 무료 Gemini API의 엔드포인트 (예시 URL)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.0-pro:generateContent?key=AIzaSyAZ-Y4dN9GriecS7zs4lLyRas93eDmuLoA";

        // POST 요청 보내기, 응답은 Map 형태로 받음 (응답 JSON: {"startTime": "09:00", "endTime": "10:00"} 가정)
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);

        // 응답이 정상적이면 JSON에서 시간 값을 파싱
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> body = response.getBody();
            String startTimeStr = (String) body.get("startTime"); // 예: "09:00"
            String endTimeStr = (String) body.get("endTime");     // 예: "10:00"
            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);
            return new LocalTime[]{startTime, endTime};
        } else {
            // API 호출 실패 시 기본 시간 (예: 09:00 ~ 10:00) 반환
            return new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(10, 0)};
        }
    }

    /**
     * 특정 할 일(taskId)의 완료 상태를 업데이트하고, 업데이트된 정보를 TaskResponseDto로 반환합니다.
     */
    @Transactional
    public TaskResponseDto updateTaskCompletion(Long taskId, boolean completed) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 할 일이 존재하지 않습니다."));
        task.setCompleted(completed);
        TaskEntity updatedTask = taskRepository.save(task);
        return new TaskResponseDto(updatedTask);
    }
}
