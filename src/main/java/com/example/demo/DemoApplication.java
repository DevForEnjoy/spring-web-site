package com.example.demo;

import com.example.demo.config.MvcConfig;
import com.example.demo.config.WebSecurityConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication

public class DemoApplication  {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


}

