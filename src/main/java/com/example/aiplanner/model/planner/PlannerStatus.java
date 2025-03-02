package com.example.aiplanner.model.planner;

public enum PlannerStatus {
    PENDING,   // 일정 생성 후 대기 상태
    ACTIVE,    // 일정 진행 중
    COMPLETED, // 완료됨
    DELETED;   // 삭제됨
}
