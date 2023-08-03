package com.lostsidewalk.cultivator;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
@Component
public class RulesEngine {

    @Autowired
    KieContainer kieContainer;

    /**
     * Evaluates the rules using the provided sensor state and actuators.
     *
     * @param sensorState the map of sensor names to their current values
     * @param actuators   the map of actuators with their names as keys
     * @param alerts      the map of alerts with the names as keys
     */
    public void evaluateRules(Map<String, Object> sensorState, Map<String, Actuator> actuators, Map<String, Alert> alerts) {
        try {
            KieSession kSession = kieContainer.newKieSession();

            // Insert sensor, actuator, and alert objects into the rule engine session
            kSession.insert(sensorState);
            kSession.insert(actuators);
            kSession.insert(alerts);

            // Fire the rules and trigger the associated actions
            kSession.fireAllRules();

            // Dispose the rule engine session
            kSession.dispose();
        } catch (Exception e) {
            log.error("An error occurred during rule evaluation due to: {}", e.getMessage());
        }
    }
}
