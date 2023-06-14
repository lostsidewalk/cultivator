package com.lostsidewalk.growmate.app;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for GPIO related beans.
 *
 * This file is part of the GrowMate project.
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
    @Bean
    public GpioController gpioController() {
        return GpioFactory.getInstance();
    }
}
