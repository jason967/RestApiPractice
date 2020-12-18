package com.example.restapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class})
@SpringBootApplication
public class RestapiApplication {

    public static void main(String[] args) {
       SpringApplication.run(RestapiApplication.class, args);

    }

    //빈을 등록, 등록한 빈은 컨트롤러에서 사용가능
   @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }

}

