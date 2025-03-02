package com.example.aiplanner.controller;

import com.example.aiplanner.model.planner.TaskRequestDto;
import com.example.aiplanner.model.planner.TaskResponseDto;
import com.example.aiplanner.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    // 할 일 추가: 사용자가 TaskRequestDto로 할 일을 입력하면 AI 연동 후 TaskResponseDto 반환
    @PostMapping
    public ResponseEntity<TaskResponseDto> addTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto responseDto = taskService.addTask(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 할 일 완료 상태 업데이트
    @PutMapping("/{taskId}/completion")
    public ResponseEntity<TaskResponseDto> updateCompletion(@PathVariable Long taskId, @RequestParam boolean completed) {
        TaskResponseDto responseDto = taskService.updateTaskCompletion(taskId, completed);
        return ResponseEntity.ok(responseDto);
    }
}
