package com.projectspace.projectspaceapi;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ProjectSpaceApiApplication {

    @PostConstruct
    public void init(){
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjectSpaceApiApplication.class, args);
    }

}
