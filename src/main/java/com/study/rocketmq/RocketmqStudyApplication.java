package com.study.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.study.rocketmq")
public class RocketmqStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqStudyApplication.class, args);
    }

}
