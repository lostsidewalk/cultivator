package com.lostsidewalk.growmate.sensors;

import com.lostsidewalk.growmate.Sensor;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Base implementation class for sensors.
 *
 * @param <T> the type of sensor value
 *
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
abstract class BaseSensorImplementation<T> implements Sensor<T> {

    private static final long DEFAULT_SENSOR_READ_TIMEOUT = 500L;

    private final String name;
    private final GpioPinDigitalInput inputPin;
    private final long timeout;
    /**
     * The current value of the sensor.
     */
    protected T value;

    /**
     * Constructs a BaseSensorImplementation with the given name and input pin.
     *
     * @param name      the name of the sensor
     * @param inputPin  the GPIO input pin associated with the sensor
     */
    BaseSensorImplementation(String name, GpioPinDigitalInput inputPin, Long timeout) {
        this.name = name;
        this.inputPin = inputPin;
        this.timeout = (timeout == null || timeout < 0) ? DEFAULT_SENSOR_READ_TIMEOUT : timeout;
    }

    /**
     * Reads the current value from the sensor.
     *
     * @return the current value of the sensor
     */
    @Override
    public T currentValue() {
        try {
            // Implement the logic to read the value from the sensor using Pi4J
            // For example, assuming the pin is connected to an analog-to-digital converter (ADC) that reads the value
            PinState pinState = readStateWithTimeout(inputPin, this.timeout);

            // Convert the digital value to sensor-specific value
            T value = convert(pinState);
            this.value = value;

            return value;
        } catch (Exception e) {
            log.error("Unable to get input pin state due to: {}", e.getMessage());
        }

        return null;
    }

    private static PinState readStateWithTimeout(GpioPinDigitalInput inputPin, @SuppressWarnings("SameParameterValue") long timeout) {
        ExecutorService executor = newSingleThreadExecutor();
        Future<PinState> future = executor.submit(inputPin::getState);

        try {
            return future.get(timeout, MILLISECONDS);
        } catch (TimeoutException e) {
            // Timeout occurred
            future.cancel(true); // Cancel the task
            return null;
        } catch (InterruptedException | ExecutionException e) {
            // Handle other exceptions
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Converts the given pin state to the sensor-specific value.
     *
     * @param pinState the pin state to convert
     * @return the sensor-specific value
     */
    protected abstract T convert(PinState pinState);

    /**
     * Returns the name of the sensor.
     *
     * @return the name of the sensor
     */
    @Override
    public String getName() {
        return this.name;
    }
}
