package com.example.aiplanner.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {
    @NotBlank(message = "사용자 이름은 필수입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min=6, message = "비밀번호는 최소 6자리 이상입니다.")
    private String userpassword;

    @NotBlank(message = "사용자 이메일은 필수입니다.")
    @Email(message = "올바르지 않는 이메일 형식입니다.")
    private String useremail;
}
