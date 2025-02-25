package com.example.aiplanner.repository;

import com.example.aiplanner.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUseremail(String useremail);
}
