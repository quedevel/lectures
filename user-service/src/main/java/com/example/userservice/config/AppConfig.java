package com.example.userservice.config;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            UserService userService;

            @Override
            public void run(ApplicationArguments args) {
                UserDto userDto = new UserDto();
                userDto.setEmail("kdh90817@naver.com");
                userDto.setName("dongho kim");
                userDto.setPwd("1234");

                userService.createUser(userDto);
            }
        };
    }
}
