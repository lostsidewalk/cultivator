package com.lostsidewalk.cultivator.sensors;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;

/**
 * Implementation class for a light sensor.
 *
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
public class LightSensor extends BaseSensorImplementation<Boolean> {

    /**
     * Creates a LightSensor object with the specified name and input pin.
     *
     * @param name      the name of the light sensor
     * @param inputPin  the GPIO digital input pin connected to the light sensor
     * @param timeout   the number of ms to wait for the sensor read to complete
     */
    public LightSensor(String name, GpioPinDigitalInput inputPin, Long timeout) {
        super(name, inputPin, timeout);
    }

    /**
     * Converts the pin state to a boolean value representing the light level.
     *
     * @param pinState  the pin state read from the input pin
     * @return          the converted boolean value indicating the light level (true for high, false for low)
     */
    @Override
    protected Boolean convert(PinState pinState) {
        return pinState.isHigh();
    }
}
