package com.lostsidewalk.growmate.app;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for rules related beans.
 *
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Configuration
public class RulesConfig {

    /**
     * Creates and returns an instance of KieContainer.
     *
     * @return an instance of KieContainer
     */
    @Bean
    KieContainer kieContainer() {
        // Load the rules from the rule files
        KieServices ks = KieServices.Factory.get();
        return ks.getKieClasspathContainer();
    }
}
