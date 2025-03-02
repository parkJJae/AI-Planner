package com.example.aiplanner.model.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {
    @NotBlank(message = "이메일을 입력하세요")
    private String useremail;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String userpassword;
}
