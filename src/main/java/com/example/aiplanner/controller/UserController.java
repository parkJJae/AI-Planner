package com.example.aiplanner.controller;

import com.example.aiplanner.model.UserEntity;
import com.example.aiplanner.model.UserRegisterDto;
import com.example.aiplanner.model.UserResponseDto;
import com.example.aiplanner.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController // RequestBody + Controller 데이터를 JSON 형식으로 응답해줌.
@RequestMapping("/users")
@RequiredArgsConstructor // final붙은 것들을 자동으로 생성자 만들어줌
public class UserController {

    private final UserService userService; // 서비스에 있는 메서드를 사용하기 위해
    //회원가입 API
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRegisterDto userRegisterDto, BindingResult bindingResult) {
        // 검증 오류가 있으면 400 Bad Request 응답을 보내고 오류 메시지를 반환
        if (bindingResult.hasErrors()) {
            // 검증 오류가 있을 경우 처리 로직 (예: 오류 메시지 반환)
            return ResponseEntity.badRequest().body(new UserResponseDto("Validation failed"));
        }
        UserEntity user = userService.CreateUser(userRegisterDto);
        return ResponseEntity.ok(new UserResponseDto(user));
    }

    // 회원 조회 API
    @GetMapping("/{userid}")
    // URL에서 {userId}에 해당하는 값을 메서드의 매개변수로 전달함. -> @PathVariable
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userid){
        UserResponseDto user = userService.getUser(userid);
        return ResponseEntity.ok(user);
    }

}
