package com.pjh.todoapp.Entity.dto.web;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$")
    @Column(unique = true)
    private String username;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String password;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    private boolean admin = false;

    private String adminToken = "";
}