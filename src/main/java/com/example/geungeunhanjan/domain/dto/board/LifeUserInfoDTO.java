package com.example.geungeunhanjan.domain.dto.board;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class LifeUserInfoDTO {
    private Long uniId;
    private Long userId;
    private Long kakaoId;
    private String nickname;
    private String email;
    private String userPassword;
    private LocalDateTime userBirth;
    private String uniAbout;
    private String userFileProfileSource;
    private String userFileBackSource;


    /* 생일 */
    private int birthYear;
    private int birthMonth;
    private int birthDay;

}
