package com.example.aiplanner.model.planner;
import com.example.aiplanner.model.planner.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String taskName;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean completed;

    public TaskResponseDto(TaskEntity taskEntity) {
        this.id = taskEntity.getId();
        this.taskName = taskEntity.getTaskName();
        //this.startTime = taskEntity.getStartTime();
        //this.endTime = taskEntity.getEndTime();
        //this.completed = taskEntity.isCompleted();
    }

}
