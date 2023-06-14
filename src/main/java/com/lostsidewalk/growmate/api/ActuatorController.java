package com.lostsidewalk.growmate.api;

import com.lostsidewalk.growmate.ActuatorDefinition;
import com.lostsidewalk.growmate.MonitorService;
import com.lostsidewalk.growmate.app.GrowMateConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller class for handling API endpoints related to actuators.
 *
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@RestController
public class ActuatorController {

    @Autowired
    GrowMateConfigProperties configProperties;

    @Autowired
    MonitorService monitorService;

    /**
     * Retrieves the list of actuator definitions.
     *
     * @return ResponseEntity containing the list of actuator definitions
     */
    @GetMapping("/actuators")
    public ResponseEntity<List<ActuatorDefinition>> getActuatorDefinitions() {
        List<ActuatorDefinition> actuatorDefinitions = configProperties.getActuatorDefinitions();
        return ok(actuatorDefinitions);
    }

    /**
     * Toggles the state of the specified actuator.
     *
     * @param name the name of the actuator
     * @return ResponseEntity indicating the success of the operation
     */
    @PutMapping("/actuators/{name}")
    public ResponseEntity<?> updateActuator(@PathVariable String name) {
        monitorService.toggleActuator(name);
        return ok().build();
    }
}
