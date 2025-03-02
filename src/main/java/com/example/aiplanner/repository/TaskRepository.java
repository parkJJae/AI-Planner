package com.example.aiplanner.repository;

import com.example.aiplanner.model.planner.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByPlanner_Id(Long plannerId);
}
