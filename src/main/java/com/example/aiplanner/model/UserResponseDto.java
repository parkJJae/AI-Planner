// 회원 조회 ( 엔티티를 Dto로 변환 )
package com.example.aiplanner.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String useremail;
    private String errorMessage;


    public UserResponseDto(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.useremail = userEntity.getUseremail();
    }

    public UserResponseDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
