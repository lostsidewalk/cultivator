package com.lostsidewalk.cultivator.app;

import com.lostsidewalk.cultivator.ActuatorDefinition;
import com.lostsidewalk.cultivator.AlertDefinition;
import com.lostsidewalk.cultivator.SensorDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for Cultivator application.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Component
@ConfigurationProperties(prefix = "com.lostsidewalk.cultivator")
public class CultivatorConfigProperties {

    private List<SensorDefinition> sensorDefinitions;
    private List<ActuatorDefinition> actuatorDefinitions;
    private List<AlertDefinition> alertDefinitions;

    /**
     * Get the list of sensor definitions.
     *
     * @return the list of sensor definitions
     */
    public List<SensorDefinition> getSensorDefinitions() {
        return sensorDefinitions;
    }

    /**
     * Set the list of sensor definitions.
     *
     * @param sensorDefinitions the list of sensor definitions
     */
    public void setSensorDefinitions(List<SensorDefinition> sensorDefinitions) {
        this.sensorDefinitions = sensorDefinitions;
    }

    /**
     * Get the list of actuator definitions.
     *
     * @return the list of actuator definitions
     */
    public List<ActuatorDefinition> getActuatorDefinitions() {
        return actuatorDefinitions;
    }

    /**
     * Set the list of actuator definitions.
     *
     * @param actuatorDefinitions the list of actuator definitions
     */
    public void setActuatorDefinitions(List<ActuatorDefinition> actuatorDefinitions) {
        this.actuatorDefinitions = actuatorDefinitions;
    }

    /**
     * Get the list of alert definitions.
     *
     * @return the list of alert definitions
     */
    public List<AlertDefinition> getAlertDefinitions() {
        return alertDefinitions;
    }

    /**
     * Set the list of alert definitions.
     *
     * @param alertDefinitions the list of alert definitions
     */
    public void setAlertDefinitions(List<AlertDefinition> alertDefinitions) {
        this.alertDefinitions = alertDefinitions;
    }
}
