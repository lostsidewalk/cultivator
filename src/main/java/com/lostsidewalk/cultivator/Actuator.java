package com.lostsidewalk.cultivator;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.PinState.LOW;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Represents an actuator.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
public class Actuator {

    private static final long DEFAULT_ACTUATOR_WRITE_TIMEOUT = 500L;

    private final String name;
    private final GpioPinDigitalOutput outputPin;
    private final long timeout;
    private final AtomicBoolean currentState = new AtomicBoolean(false);

    /**
     * Constructs an Actuator with the given name and output pin.
     *
     * @param name       the name of the actuator
     * @param outputPin  the GPIO output pin associated with the actuator
     * @param timeout    the number of milliseconds to wait for the actuator write to complete
     */
    public Actuator(String name, GpioPinDigitalOutput outputPin, Long timeout) {
        this.name = name;
        this.outputPin = outputPin;
        this.timeout = (timeout == null || timeout < 0) ? DEFAULT_ACTUATOR_WRITE_TIMEOUT : timeout;
    }

    /**
     * Sets the state of the actuator based on the provided boolean value.
     *
     * @param state  the desired state of the actuator
     */
    @SuppressWarnings("unused")
    public void setState(boolean state) {
        // Set the state of the actuator pin based on the provided boolean value
        setStateWithTimeout(outputPin, state ? HIGH : LOW, timeout, currentState);
    }

    private static void setStateWithTimeout(GpioPinDigitalOutput pin, PinState pinState, @SuppressWarnings("SameParameterValue") long timeout, AtomicBoolean currentState) {
        // Create an ExecutorService
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the task to set the state
        Future<Void> future = executor.submit(() -> {
            pin.setState(pinState);
            currentState.set(pinState.isHigh());
            return null;
        });

        try {
            // Wait for the task to complete within the timeout duration
            future.get(timeout, MILLISECONDS);
            log.debug("State set successfully.");
        } catch (TimeoutException e) {
            log.error("Timeout occurred while setting the state due to: {}", e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            log.error("An error occurred while setting the state due to: {}", e.getMessage());
        } finally {
            // Shutdown the executor
            executor.shutdown();
        }
    }

    /**
     * Returns the name of the actuator.
     *
     * @return the name of the actuator
     */
    public String getName() {
        return this.name;
    }

    /**
     * Toggles the state of the actuator.
     */
    public void toggleState() {
        // sorry
        setStateWithTimeout(outputPin, PinState.getState(!this.currentState.get()), timeout, this.currentState);
    }

    /**
     * Creates and returns an Actuator with the given name, output pin, and timeout.
     *
     * @param name       the name of the actuator
     * @param outputPin  the GPIO output pin associated with the actuator
     * @param timeout    the number of milliseconds to wait for the actuator write to complete
     * @return an Actuator instance
     */
    public static Actuator from(String name, GpioPinDigitalOutput outputPin, Long timeout) {
        return new Actuator(name, outputPin, timeout);
    }

    @Override
    public String toString() {
        return String.format("[name=%s,timeout=%d]", name, timeout);
    }
}
