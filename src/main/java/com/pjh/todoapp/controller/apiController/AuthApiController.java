package com.pjh.todoapp.controller.apiController;

import com.pjh.todoapp.Entity.CMRespDto;
import com.pjh.todoapp.Entity.web.LoginRequestDto;
import com.pjh.todoapp.Entity.web.SignupRequestDto;
import com.pjh.todoapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final UserService userService;

    @PostMapping("/api/user/signup")
    public ResponseEntity<?> signup(@Valid SignupRequestDto signupRequestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(new CMRespDto<>(HttpStatus.BAD_REQUEST.value(),"회원가입 실패",errorMap),HttpStatus.BAD_REQUEST);
        }
        userService.회원가입(signupRequestDto);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/login").body("회원가입 성공");
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@Valid LoginRequestDto loginRequestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Location", "/login").body("로그인 실패");
        }
        redirectAttributes.addFlashAttribute("loginSuccess", true);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/").body("로그인 성공");
    }
}
