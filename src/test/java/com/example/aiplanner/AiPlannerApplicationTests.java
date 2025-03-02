package com.example.aiplanner;

import com.example.aiplanner.model.user.UserEntity;
import com.example.aiplanner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiPlannerApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testJpa() {
        UserEntity user = new UserEntity();
        user.setUseremail("wgpark0830@naver.com");
        user.setUserpassword("1234");
        this.userRepository.save(user);

    }

}
