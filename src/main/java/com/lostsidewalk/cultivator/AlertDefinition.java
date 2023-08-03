package com.lostsidewalk.cultivator;

import java.io.Serializable;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
public class AlertDefinition implements Serializable {

    public static final long serialVersionUID = 209821231493L;

    private String name;

    /**
     * Creates an AlertDefinition object with the specified name and pin address.
     *
     * @param name         the name of the alert
     */
    public AlertDefinition(String name) {
        this.name = name;
    }

    /**
     * Creates an AlertDefinition object with no property values.
     */
    public AlertDefinition() {}

    /**
     * Returns the name of the alert.
     *
     * @return the name of the alert
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the alert.
     *
     * @param name the name of the alert
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
