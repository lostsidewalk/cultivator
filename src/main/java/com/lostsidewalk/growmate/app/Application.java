package com.lostsidewalk.growmate.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main class for the GrowMate application.
 *
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Configuration
@EnableAutoConfiguration
@EnableScheduling
@EnableConfigurationProperties
@ComponentScan({"com.lostsidewalk.growmate", "com.lostsidewalk.growmate.app"})
public class Application {

    /**
     * The entry point of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("pi4j.library.path", "system");
        SpringApplication.run(Application.class, args);
    }
}
