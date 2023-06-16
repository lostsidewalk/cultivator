package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.ActuatorDefinition;
import com.lostsidewalk.cultivator.MonitorService;
import com.lostsidewalk.cultivator.MonitorService.ActuatorNotFoundException;
import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller class for handling API endpoints related to actuators.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
@RestController
public class ActuatorController {

    @Autowired
    CultivatorConfigProperties configProperties;

    @Autowired
    MonitorService monitorService;

    /**
     * Retrieves the list of actuator definitions.
     *
     * @return ResponseEntity containing the list of actuator definitions
     */
    @GetMapping("/actuators")
    public ResponseEntity<List<ActuatorDefinition>> getActuatorDefinitions() {
        log.info("Fetching actuator definitions");
        List<ActuatorDefinition> actuatorDefinitions = configProperties.getActuatorDefinitions();
        return ok(actuatorDefinitions);
    }

    @GetMapping("/actuators/current")
    public ResponseEntity<Map<String, ?>> getCurrentStates() {
        log.info("Fetching actuator current states");
        Map<String, Boolean> currentStates = monitorService.getActuatorStates();
        return ok(currentStates);
    }

    /**
     * Retrieves the current state of the specified actuator.
     *
     * @param actuatorName the name of the actuator
     * @return ResponseEntity containing the current state of the actuator
     */
    @GetMapping("/actuators/current/{name}")
    public ResponseEntity<?> getCurrentState(@PathVariable("name") String actuatorName) throws ActuatorNotFoundException {
        log.info("Fetching actuator current state, name={}", actuatorName);
        Boolean currentState = monitorService.getActuatorState(actuatorName);
        return ok(currentState);
    }

    /**
     * Enables the specified actuator.
     *
     * @param name the name of the actuator
     * @return ResponseEntity indicating the success of the operation
     */
    @PutMapping("/actuators/{name}/enable")
    public ResponseEntity<?> enableActuator(@PathVariable String name) {
        log.info("Enabling actuator, name={}", name);
        monitorService.enableActuator(name);
        return ok().build();
    }

    /**
     * Enables the specified actuator.
     *
     * @param name the name of the actuator
     * @return ResponseEntity indicating the success of the operation
     */
    @PutMapping("/actuators/{name}/disable")
    public ResponseEntity<?> disableActuator(@PathVariable String name) {
        log.info("Disabling actuator, name={}", name);
        monitorService.disableActuator(name);
        return ok().build();
    }
}
