package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.MonitorService;
import com.lostsidewalk.cultivator.MonitorService.SensorNotFoundException;
import com.lostsidewalk.cultivator.SensorDefinition;
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
 * Controller class for handling API endpoints related to sensors.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
@RestController
public class SensorController {

    @Autowired
    CultivatorConfigProperties configProperties;

    @Autowired
    MonitorService monitorService;

    /**
     * Retrieves the list of sensor definitions.
     *
     * @return ResponseEntity containing the list of sensor definitions
     */
    @GetMapping("/sensors")
    public ResponseEntity<List<SensorDefinition>> getSensorDefinitions() {
        log.info("Fetching sensor definitions");
        List<SensorDefinition> sensorDefinitions = configProperties.getSensorDefinitions();
        return ok(sensorDefinitions);
    }

    @GetMapping("/sensors/current")
    public ResponseEntity<Map<String, ?>> getCurrentValues() {
        log.info("Fetching sensor current values");
        Map<String, Object> currentValues = monitorService.getSensorState();
        return ok(currentValues);
    }

    /**
     * Retrieves the current value of the specified sensor.
     *
     * @param sensorName the name of the sensor
     * @return ResponseEntity containing the current value of the sensor
     */
    @GetMapping("/sensors/current/{name}")
    public ResponseEntity<?> getCurrentValue(@PathVariable("name") String sensorName) throws SensorNotFoundException {
        log.info("Fetching sensor current value, sensorName={}", sensorName);
        Object currentValue = monitorService.getSensorValue(sensorName);
        return ok(currentValue);
    }
}
