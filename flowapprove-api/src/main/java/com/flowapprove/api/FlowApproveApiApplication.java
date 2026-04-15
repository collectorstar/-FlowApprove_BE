package com.flowapprove.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.flowapprove")
public class FlowApproveApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowApproveApiApplication.class, args);
    }
}
