package com.lostsidewalk.cultivator.app;

import com.lostsidewalk.cultivator.GpioAdapter;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration class for GPIO related beans.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Configuration
public class GpioConfig {

    /**
     * Creates and returns an instance of GpioController.
     *
     * @return an instance of GpioController
     */
    @Profile("micro")
    @Bean
    public GpioAdapter deviceGpioController() {
        return new GpioAdapter(GpioFactory.getInstance());
    }

    @Bean
    public GpioAdapter defaultGpioController() {
        return new GpioAdapter();
    }
}
