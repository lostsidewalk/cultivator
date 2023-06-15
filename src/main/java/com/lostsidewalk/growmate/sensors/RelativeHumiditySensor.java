package com.lostsidewalk.growmate.sensors;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;

import java.math.BigDecimal;

/**
 * Implementation class for a relative humidity sensor.
 *
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
public class RelativeHumiditySensor extends BaseSensorImplementation<BigDecimal> {

    /**
     * Creates a RelativeHumiditySensor object with the specified name and input pin.
     *
     * @param name      the name of the relative humidity sensor
     * @param inputPin  the GPIO digital input pin connected to the relative humidity sensor
     * @param timeout   the number of ms to wait for the sensor read to complete
     */
    public RelativeHumiditySensor(String name, GpioPinDigitalInput inputPin, Long timeout) {
        super(name, inputPin, timeout);
    }

    /**
     * Converts the pin state to an integer value representing the relative humidity.
     *
     * @param pinState  the pin state read from the input pin
     * @return          the converted integer value representing the relative humidity
     */
    @Override
    protected BigDecimal convert(PinState pinState) {
        return null;
    }
}
