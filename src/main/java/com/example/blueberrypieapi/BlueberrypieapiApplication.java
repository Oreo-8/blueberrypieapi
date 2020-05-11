package com.example.blueberrypieapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableRabbit
@MapperScan("com.example.blueberrypieapi.mapper")
public class BlueberrypieapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlueberrypieapiApplication.class, args);
    }

}
