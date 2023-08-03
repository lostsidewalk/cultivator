package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.Alert.AlertState;
import com.lostsidewalk.cultivator.AlertDefinition;
import com.lostsidewalk.cultivator.MonitorService;
import com.lostsidewalk.cultivator.MonitorService.AlertNotFoundException;
import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller class for handling API endpoints related to alerts.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
@RestController
public class AlertController {

    @Autowired
    CultivatorConfigProperties configProperties;

    @Autowired
    MonitorService monitorService;

    /**
     * Retrieves the list of alert definitions.
     *
     * @return ResponseEntity containing the list of alert definitions
     */
    @GetMapping("/alerts")
    public ResponseEntity<List<AlertDefinition>> getAlertDefinitions() {
        log.info("Fetching alert definitions");
        List<AlertDefinition> alertDefinitions = configProperties.getAlertDefinitions();
        return ok(alertDefinitions);
    }

    @GetMapping("/alerts/current")
    public ResponseEntity<Map<String, ?>> getCurrentStates() {
        log.info("Fetching alert current states");
        Map<String, AlertState> currentStates = monitorService.getAlertStates();
        return ok(currentStates);
    }

    /**
     * Retrieves the current state of the specified alert.
     *
     * @param alertName the name of the alert
     * @return ResponseEntity containing the current state of the alert
     */
    @GetMapping("/alerts/current/{name}")
    public ResponseEntity<?> getCurrentState(@PathVariable("name") String alertName) throws AlertNotFoundException {
        log.info("Fetching alert current state, name={}", alertName);
        AlertState currentState = monitorService.getAlertState(alertName);
        return ok(currentState);
    }
}
