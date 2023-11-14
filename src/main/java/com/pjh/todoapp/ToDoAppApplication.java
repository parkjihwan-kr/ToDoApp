package com.pjh.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ToDoAppApplication {
    // 잠시 Security부분을 막음
    public static void main(String[] args) {
        SpringApplication.run(ToDoAppApplication.class, args);
    }

}
