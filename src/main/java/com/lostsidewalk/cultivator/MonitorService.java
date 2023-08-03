package com.lostsidewalk.cultivator;

import com.lostsidewalk.cultivator.Alert.AlertState;
import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import com.lostsidewalk.cultivator.sensors.*;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static com.pi4j.io.gpio.PinPullResistance.PULL_DOWN;
import static com.pi4j.io.gpio.PinState.LOW;
import static com.pi4j.io.gpio.RaspiPin.getPinByAddress;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Locale.ROOT;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.size;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@Slf4j
@Service
public class MonitorService {

    @Autowired
    CultivatorConfigProperties configProperties;

    @Autowired
    RulesEngine rulesEngine;

    @Autowired
    GpioAdapter gpioAdapter;

    /**
     * Represents the types of sensors available.
     */
    enum SensorType {
        AIR_QUALITY("aq", AirQualitySensor.class),
        LIGHT("light", LightSensor.class),
        RELATIVE_HUMIDITY("rh", RelativeHumiditySensor.class),
        RESERVOIR_PH("rph", ReservoirPhSensor.class),
        SOIL("soil", SoilSensor.class),
        TEMPERATURE("temp", TemperatureSensor.class);

        final String name;
        final Class<? extends Sensor<?>> clazz;

        SensorType(String name, Class<? extends Sensor<?>> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        /**
         * Retrieves the SensorType based on the given name.
         *
         * @param name the name of the SensorType
         * @return the corresponding SensorType, or null if not found
         */
        static SensorType byName(String name) {
            if (isNotBlank(name)) {
                String n = name.trim().toLowerCase(ROOT);
                for (SensorType s : values()) {
                    if (s.name.equals(n)) {
                        return s;
                    }
                }
            }
            return null;
        }
    }

    List<Sensor<?>> sensors;
    Map<String, Actuator> actuators;
    Map<String, Alert> alerts;

    /**
     * Initializes the MonitorService after construction.
     */
    @PostConstruct
    public void postConstruct() {
        sensors = buildSensors();
        log.info("Cultivator sensors={}", sensors);

        actuators = buildActuators(gpioAdapter);
        log.info("Cultivator actuators={}", actuators);

        alerts = buildAlerts();
        log.info("Cultivator alerts={}", alerts);

        log.info("Starting Cultivator reactor...");
        Flux.interval(ofSeconds(2L))
                .map(tick -> getSensorState())
                .subscribe(sensorState -> rulesEngine.evaluateRules(sensorState, actuators, alerts));
    }

    /**
     * Builds the list of sensors based on the configuration properties.
     *
     * @return the list of built sensors
     */
    List<Sensor<?>> buildSensors() {
        List<SensorDefinition> sensorDefinitions = configProperties.getSensorDefinitions();
        if (isNotEmpty(sensorDefinitions)) {
            List<Sensor<?>> sensors = newArrayListWithExpectedSize(size(sensorDefinitions));
            for (SensorDefinition sensorDefinition : sensorDefinitions) {
                String sensorTypeName = trim(sensorDefinition.getType());
                SensorType sensorType = SensorType.byName(sensorTypeName);
                if (sensorType != null) {
                    Sensor<?> sensor = createSensor(sensorType, sensorDefinition.getName(), sensorDefinition.getPinAddress(), sensorDefinition.getTimeout());
                    if (sensor != null) {
                        sensors.add(sensor);
                    }
                } else {
                    log.warn("Unknown sensor type name={}, skipping sensorDefinition={}", sensorTypeName, sensorDefinition);
                }
            }
            return sensors;
        }
        return emptyList();
    }

    /**
     * Creates a sensor of the specified type with the given name and pin address.
     *
     * @param sensorType    the type of the sensor
     * @param name          the name of the sensor
     * @param pinAddress    the address of the pin associated with the sensor
     * @return the created sensor
     */
    Sensor<?> createSensor(SensorType sensorType, String name, Integer pinAddress, Long timeout) {
        try {
            Constructor<? extends Sensor<?>> ctor = sensorType.clazz.getConstructor(String.class, GpioPinDigitalInput.class, Long.class);
            GpioPinDigitalInput inputPin = gpioAdapter.provisionDigitalInputPin(getPinByAddress(pinAddress), PULL_DOWN);
            return ctor.newInstance(name, inputPin, timeout);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            log.error("Unable to create sensor due to: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Builds a mapping of each sensor to its current value.
     *
     * @return the map of sensor names to their current values
     */
    public Map<String, Object> getSensorState() {
        Map<String, Object> m = new HashMap<>();
        for (Sensor<?> sensor : sensors) {
            Object currentValue = sensor.currentValue();
            m.put(sensor.getName(), currentValue);
        }
        return m;
    }

    public static class SensorNotFoundException extends Exception {

        public SensorNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Returns the current value of any sensor given by name.
     *
     * @param name the name of the sensor to query
     * @return the current value of the named sensor
     */
    public Object getSensorValue(String name) throws SensorNotFoundException {
        Sensor<?> sensor = sensors.stream().filter(s -> StringUtils.equals(s.getName(), name))
                .findFirst()
                .orElseThrow(() -> new SensorNotFoundException(name));
        return sensor.currentValue();
    }

    /**
     * Builds the map of actuators based on the configuration properties.
     *
     * @param gpioAdapter the GpioController instance
     * @return the map of actuators with their names as keys
     */
    Map<String, Actuator> buildActuators(GpioAdapter gpioAdapter) {
        List<ActuatorDefinition> actuatorDefinitions = configProperties.getActuatorDefinitions();
        if (isNotEmpty(actuatorDefinitions)) {
            List<Actuator> actuators = newArrayListWithExpectedSize(size(actuatorDefinitions));
            for (ActuatorDefinition actuatorDefinition : actuatorDefinitions) {
                try {
                    GpioPinDigitalOutput outputPin = gpioAdapter.provisionDigitalOutputPin(getPinByAddress(actuatorDefinition.getPinAddress()), LOW);
                    actuators.add(Actuator.from(
                            actuatorDefinition.getName(),
                            outputPin,
                            actuatorDefinition.getTimeout(),
                            actuatorDefinition.getInitialState()
                    ) );
                } catch (Exception e) {
                    log.error("Unable to provision digital output pin due to: {}", e.getMessage());
                }
            }
            return actuators.stream().collect(toMap(Actuator::getName, a -> a));
        }
        return emptyMap();
    }

    /**
     * Enables the specified actuator.
     *
     * @param name the name of the actuator
     */
    public void enableActuator(String name) {
        Actuator actuator = this.actuators.get(name);
        if (actuator != null) {
            actuator.setState(true);
        }
    }

    /**
     * Disables the specified actuator.
     *
     * @param name the name of the actuator
     */
    public void disableActuator(String name) {
        Actuator actuator = this.actuators.get(name);
        if (actuator != null) {
            actuator.setState(false);
        }
    }

    public Map<String, Boolean> getActuatorStates() {
        return actuators.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, v -> v.getValue().getState()));
    }

    public static class ActuatorNotFoundException extends Exception {
        public ActuatorNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Returns the current state of any actuator given by name.
     *
     * @param name the name of the actuator to query
     * @return the current state of the named actuator
     */
    public Boolean getActuatorState(String name) throws ActuatorNotFoundException {
        if (actuators.containsKey(name)) {
            return actuators.get(name).getState();
        }
        throw new ActuatorNotFoundException(name);
    }

    /**
     * Builds the map of alerts based on the configuration properties.
     *
     * @return the map of alerts with their names as keys
     */
    Map<String, Alert> buildAlerts() {
        List<AlertDefinition> alertDefinitions = configProperties.getAlertDefinitions();
        if (isNotEmpty(alertDefinitions)) {
            List<Alert> alerts = newArrayListWithExpectedSize(size(alertDefinitions));
            for (AlertDefinition alertDefinition : alertDefinitions) {
                alerts.add(Alert.from(alertDefinition.getName()) );
            }
            return alerts.stream().collect(toMap(Alert::getName, a -> a));
        }
        return emptyMap();
    }

    public Map<String, AlertState> getAlertStates() {
        return alerts.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, v -> v.getValue().getAlertState()));
    }

    public static class AlertNotFoundException extends Exception {
        public AlertNotFoundException(String message) {
            super(message);
        }
    }

    public AlertState getAlertState(String name) throws AlertNotFoundException {
        if (alerts.containsKey(name)) {
            return alerts.get(name).getAlertState();
        }
        throw new AlertNotFoundException(name);
    }
}
