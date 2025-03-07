package com.example.aiplanner.model.planner;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 여러 TaskEntity가 하나의 PlannerEntity에 속할 수 있음
    @JoinColumn(name = "planner_id", nullable = false)
    private PlannerEntity planner; // 이 할 일이 속한 플래너(일정)

    @Column(nullable = false)
    private String taskName; // 사용자가 입력한 할 일의 이름 (예: "운동하기", "개발 공부")

    // AI가 자동으로 할 일에 배정할 시작 시간
   // private LocalTime startTime;

    // AI가 자동으로 할 일에 배정할 종료 시간
    //private LocalTime endTime;

    //@Column(nullable = false)
    //private boolean completed; // 할 일이 완료되었는지 여부를 체크 (To-Do List처럼 관리)
}
