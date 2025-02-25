package com.example.aiplanner.service;

import com.example.aiplanner.model.UserEntity;
import com.example.aiplanner.model.UserRegisterDto;
import com.example.aiplanner.model.UserResponseDto;
import com.example.aiplanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Dto를 엔티티로 변환
    public UserEntity CreateUser(UserRegisterDto userRegisterDto) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userRegisterDto.getUsername());
            userEntity.setUseremail(userRegisterDto.getUseremail());
            userEntity.setUserpassword(passwordEncoder.encode(userRegisterDto.getUserpassword()));
            this.userRepository.save(userEntity);
            return userEntity;
    }

    public UserResponseDto getUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
                // 예외 처리 사용자 아이디가 존재하지 않을 때
        return new UserResponseDto(userEntity); // Entity → DTO 변환 후 반환
    }
    }
