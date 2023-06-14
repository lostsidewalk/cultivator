package com.lostsidewalk.growmate.sensors;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;

import java.math.BigDecimal;

/**
 * Represents a soil moisture sensor for measuring the moisture level in the soil.
 *
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
public class SoilSensor extends BaseSensorImplementation<BigDecimal> {

    /**
     * Constructs a SoilSensor with the given name and input pin.
     *
     * @param name      the name of the sensor
     * @param inputPin  the GPIO input pin associated with the sensor
     * @param timeout   the number of ms to wait for the sensor read to complete
     */
    public SoilSensor(String name, GpioPinDigitalInput inputPin, Long timeout) {
        super(name, inputPin, timeout);
    }

    @Override
    protected BigDecimal convert(PinState pinState) {
        // Implement the logic to convert the pin state to the soil moisture value
        // For example, use an analog-to-digital converter (ADC) to read the voltage and convert it to a moisture value
        // Return the calculated moisture value
        return null;
    }
}
