package com.example.aiplanner.model.planner;

import com.example.aiplanner.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
public class PlannerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEntity> tasks = new ArrayList<>();

   @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

   @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private boolean aiGenerated;

   @Column(nullable = false)
   private boolean deleted;

    @Column(nullable = false)
    private String plannerTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlannerStatus status = PlannerStatus.PENDING; // 기본값 설정

    // 기본 생성자에서 초기화
    public PlannerEntity() {
       this.status = PlannerStatus.PENDING; // 기본값 설정
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
