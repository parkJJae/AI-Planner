package com.example.aiplanner.repository;

import com.example.aiplanner.model.planner.PlannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannerRepository extends JpaRepository<PlannerEntity, Long> {
    List<PlannerEntity>  findByUser_Userid(Long userid);
}
