package com.kof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class UpperCaseJsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpperCaseJsonApplication.class, args);
    }
}
