package com.pjh.todoapp.Entity.dto.web;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$",
            message="Username must be a minimum of 4 characters and a maximum of 10 characters," +
                    " consisting of lowercase letters")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message="Password must be a minimum of 8 characters and a maximum of 15 characters," +
            " consisting of both uppercase and lowercase letters and numbers (0~9).")
    private String password;

    /*
    - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
    */

}
