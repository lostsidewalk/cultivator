package com.lostsidewalk.cultivator;

import java.io.Serializable;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
public class ActuatorDefinition implements Serializable {

    public static final long serialVersionUID = 209823493L;

    private String name;
    private Integer pinAddress;
    private Long timeout;

    /**
     * Creates an ActuatorDefinition object with the specified name and pin address.
     *
     * @param name        the name of the actuator
     * @param pinAddress  the pin address of the actuator
     * @param timeout     the number of MS to wait for the actuator write to complete
     */
    public ActuatorDefinition(String name, Integer pinAddress, Long timeout) {
        this.name = name;
        this.pinAddress = pinAddress;
        this.timeout = timeout;
    }

    /**
     * Creates an ActuatorDefinition object with the specified name and pin address.
     *
     * @param name        the name of the actuator
     * @param pinAddress  the pin address of the actuator
     */
    public ActuatorDefinition(String name, Integer pinAddress) {
        this.name = name;
        this.pinAddress = pinAddress;
    }

    /**
     * Creates an ActuatorDefinition object with no property values.
     */
    public ActuatorDefinition() {}

    /**
     * Returns the name of the actuator.
     *
     * @return the name of the actuator
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the actuator.
     *
     * @param name the name of the actuator
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the pin address of the actuator.
     *
     * @return the pin address of the actuator
     */
    public Integer getPinAddress() {
        return pinAddress;
    }

    /**
     * Sets the pin address of the actuator.
     *
     * @param pinAddress the pin address of the actuator
     */
    public void setPinAddress(Integer pinAddress) {
        this.pinAddress = pinAddress;
    }

    /**
     * Returns the timeout value of the actuator (ms).
     *
     * @return the timeout value of the actuator (ms).
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout value of the actuator (ms).
     *
     * @param timeout the timeout value of the actuator (ms).
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
