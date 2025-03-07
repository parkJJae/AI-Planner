package com.example.aiplanner.model.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PlannerResponseDto {
    private Long id;
    private String plannerTitle;
    private boolean completed;
    private boolean aiGenerated;
    private boolean deleted;
    private String status; // PlannerStatus의 String 값으로 변환
    private List<TaskResponseDto> tasks;

    // 생성자
    public PlannerResponseDto(PlannerEntity planner) {
        this.id = planner.getId();
        this.plannerTitle = planner.getPlannerTitle();
        //this.completed = planner.isCompleted();
        this.aiGenerated = planner.isAiGenerated();
        //this.deleted = planner.isDeleted();
        // status가 null일 경우 "UNKNOWN"으로 설정
        //this.status = (planner.getStatus() != null) ? planner.getStatus().toString() : "UNKNOWN";
        this.tasks = planner.getTasks().stream()
                .map(TaskResponseDto::new) // TaskEntity -> TaskResponseDto 변환
                .collect(Collectors.toList());
    }
}
