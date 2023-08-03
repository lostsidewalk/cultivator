package com.lostsidewalk.cultivator;

import lombok.extern.slf4j.Slf4j;

import static com.lostsidewalk.cultivator.Alert.AlertState.*;

/**
 * Represents an alert.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
public class Alert {

    public enum AlertState {
        DEBUG, INFO, WARN, ERROR;
    }

    private final String name;

    private volatile AlertState currentState;

    /**
     * Constructs an Alert with the given name and output pin.
     *
     * @param name         the name of the alert

     */
    public Alert(String name) {
        this.name = name;
    }

    public void clear() {
        this.currentState = null;
    }

    public void debug() {
        this.currentState = DEBUG;
    }

    public void info() {
        this.currentState = INFO;
    }

    public void warn() {
        this.currentState = WARN;
    }

    public void error() {
        this.currentState = ERROR;
    }

    /**
     * Returns the name of the alert.
     *
     * @return the name of the alert
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the current state of tha alert.
     *
     * @return an AlertState enum representing the current state of the alert
     */
    public AlertState getAlertState() {
        return this.currentState;
    }

    /**
     * Creates and returns an Alert with the given name, output pin, and timeout.
     *
     * @param name       the name of the alert
     * @return an Alert instance
     */
    public static Alert from(String name) {
        return new Alert(name);
    }

    @Override
    public String toString() {
        return String.format("[name=%s]", name);
    }
}
