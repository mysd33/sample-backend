package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.xray.AWSXRay;

@SpringBootApplication
public class SampleBackendApplication {

    public static void main(String[] args) {
        AWSXRay.beginSegment("sample-backend");
        SpringApplication.run(SampleBackendApplication.class, args);
    }

}
