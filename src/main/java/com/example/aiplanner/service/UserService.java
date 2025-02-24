package com.example.aiplanner.service;

import com.example.aiplanner.model.UserEntity;
import com.example.aiplanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity Create(String username, String userpassword, String useremail){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setUseremail(useremail);
        userEntity.setUserpassword(passwordEncoder.encode(userpassword));
        this.userRepository.save(userEntity);
        return userEntity;
    }
}
