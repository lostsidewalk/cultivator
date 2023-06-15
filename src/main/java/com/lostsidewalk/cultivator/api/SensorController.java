package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.MonitorService;
import com.lostsidewalk.cultivator.SensorDefinition;
import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.notFound;
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
    private CultivatorConfigProperties configProperties;

    @Autowired
    private MonitorService monitorService;

    /**
     * Retrieves the list of sensor definitions.
     *
     * @return ResponseEntity containing the list of sensor definitions
     */
    @GetMapping("/sensors")
    public ResponseEntity<List<SensorDefinition>> getSensorDefinitions() {
        List<SensorDefinition> sensorDefinitions = configProperties.getSensorDefinitions();
        return ok(sensorDefinitions);
    }

    /**
     * Retrieves the current value of the specified sensor.
     *
     * @param sensorName the name of the sensor
     * @return ResponseEntity containing the current value of the sensor
     */
    @GetMapping("/sensors/current/{name}")
    public ResponseEntity<?> getCurrentValue(@PathVariable("name") String sensorName) {
        Object currentValue = monitorService.getSensorValue(sensorName);
        if (currentValue != null) {
            return ok(currentValue);
        } else {
            return notFound().build();
        }
    }
}
