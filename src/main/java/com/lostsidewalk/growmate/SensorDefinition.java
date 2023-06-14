package com.lostsidewalk.growmate;

/**
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
public class SensorDefinition {

    private String name;
    private String type;
    private Integer pinAddress;
    private Long timeout;

    /**
     * Creates a SensorDefinition object with the specified name, type, pin address, and timeout.
     *
     * @param name        the name of the sensor
     * @param type        the type of the sensor
     * @param pinAddress  the pin address of the sensor
     * @param timeout     the number of MS to wait for a response before timing out the sensor read
     */
    public SensorDefinition(String name, String type, Integer pinAddress, Long timeout) {
        this.name = name;
        this.type = type;
        this.pinAddress = pinAddress;
        this.timeout = timeout;
    }

    /**
     * Creates a SensorDefinition object with the specified name, type, and pin address, using the default timeout.
     *
     * @param name        the name of the sensor
     * @param type        the type of the sensor
     * @param pinAddress  the pin address of the sensor
     */
    public SensorDefinition(String name, String type, Integer pinAddress) {
        this.name = name;
        this.type = type;
        this.pinAddress = pinAddress;
    }

    /**
     * Creates a sensor definition object with no property values.
     */
    public SensorDefinition() {}

    /**
     * Returns the name of the sensor.
     *
     * @return the name of the sensor
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the sensor.
     *
     * @param name the name of the sensor
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the sensor.
     *
     * @return the type of the sensor
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the sensor.
     *
     * @param type the type of the sensor
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the pin address of the sensor.
     *
     * @return the pin address of the sensor
     */
    public Integer getPinAddress() {
        return pinAddress;
    }

    /**
     * Sets the pin address of the sensor.
     *
     * @param pinAddress the pin address of the sensor
     */
    public void setPinAddress(Integer pinAddress) {
        this.pinAddress = pinAddress;
    }

    /**
     * Returns the timeout value of the sensor (ms).
     *
     * @return the timeout value of the sensor (ms).
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout value of the sensor (ms).
     *
     * @param timeout the timeout value of the sensor (ms).
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
