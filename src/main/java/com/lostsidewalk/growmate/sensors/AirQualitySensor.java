package com.lostsidewalk.growmate.sensors;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;

/**
 * Implementation class for an air quality sensor.
 *
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
public class AirQualitySensor extends BaseSensorImplementation<Boolean> {

    /**
     * Constructs an AirQualitySensor with the given name and input pin.
     *
     * @param name      the name of the air quality sensor
     * @param inputPin  the GPIO input pin associated with the air quality sensor
     * @param timeout   the number of ms to wait for the sensor read to complete
     */
    public AirQualitySensor(String name, GpioPinDigitalInput inputPin, Long timeout) {
        super(name, inputPin, timeout);
    }

    /**
     * Converts the given pin state to a Boolean value representing the air quality.
     *
     * @param pinState the pin state to convert
     * @return true if the air quality is high, false otherwise
     */
    @Override
    protected Boolean convert(PinState pinState) {
        return pinState.isHigh();
    }
}
