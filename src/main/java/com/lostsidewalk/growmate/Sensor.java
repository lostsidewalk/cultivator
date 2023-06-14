package com.lostsidewalk.growmate;

/**
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
public interface Sensor<T> {

    /**
     * Returns the name of the sensor.
     *
     * @return the name of the sensor
     */
    String getName();

    /**
     * Returns the current value of the sensor.
     *
     * @return the current value of the sensor
     */
    T currentValue();
}
