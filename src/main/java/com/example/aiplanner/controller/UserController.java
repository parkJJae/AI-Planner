package com.example.aiplanner.controller;

import com.example.aiplanner.model.user.UserEntity;
import com.example.aiplanner.model.user.UserLoginDto;
import com.example.aiplanner.model.user.UserRegisterDto;
import com.example.aiplanner.model.user.UserResponseDto;
import com.example.aiplanner.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // RequestBody + Controller 데이터를 JSON 형식으로 응답해줌.
@RequestMapping("/users")
@RequiredArgsConstructor // final붙은 것들을 자동으로 생성자 만들어줌
public class UserController {

    private final UserService userService; // 서비스에 있는 메서드를 사용하기 위해
    //회원가입 API
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDto userRegisterDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", "입력값이 올바르지 않습니다.")); // JSON 형태로 응답 반환
        }
        try {
            UserEntity user = userService.CreateUser(userRegisterDto);
            return ResponseEntity.ok(new UserResponseDto(user)); // 항상 JSON 형식 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "회원가입 중 오류 발생"));
        }
    }


    // 회원 조회 API
    @GetMapping("/{userid}")
    // URL에서 {userId}에 해당하는 값을 메서드의 매개변수로 전달함. -> @PathVariable
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userid){
        UserResponseDto user = userService.getUser(userid);
        return ResponseEntity.ok(user);
    }

    // 로그인 API
    @PostMapping("/login")
    // <?> -> 반환 타입을 어떤 걸로든 사용할 수 있음.
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("입력값이 올바르지 않습니다.");
        }

        try{ // 예외처리
            UserResponseDto userResponseDto = userService.loginUser(userLoginDto);
            return ResponseEntity.ok(userResponseDto);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
