package com.lostsidewalk.cultivator;

import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import com.lostsidewalk.cultivator.sensors.*;
import com.pi4j.io.gpio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lostsidewalk.cultivator.MonitorService.SensorType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
class MonitorServiceTest {

    @Mock
    private CultivatorConfigProperties configProperties;

    @Mock
    private RulesEngine rulesEngine;

    @Mock
    private GpioAdapter gpioAdapter;

    @InjectMocks
    private MonitorService monitorService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        monitorService = new MonitorService();
        monitorService.configProperties = configProperties;
        monitorService.rulesEngine = rulesEngine;
        monitorService.gpioAdapter = gpioAdapter;
    }

    @Test
    void testPostConstruct_BuildSensors() {
        List<SensorDefinition> sensorDefinitions = new ArrayList<>();
        sensorDefinitions.add(new SensorDefinition("Air Quality Sensor", AIR_QUALITY.name, 17));
        sensorDefinitions.add(new SensorDefinition("Light Sensor", LIGHT.name, 18));
        sensorDefinitions.add(new SensorDefinition("RH Sensor", RELATIVE_HUMIDITY.name, 19));
        sensorDefinitions.add(new SensorDefinition("Reservoir pH Sensor", RESERVOIR_PH.name, 20));
        sensorDefinitions.add(new SensorDefinition("Soil Sensor", SOIL.name, 21));
        sensorDefinitions.add(new SensorDefinition("Temperature Sensor", TEMPERATURE.name, 22));

        when(configProperties.getSensorDefinitions()).thenReturn(sensorDefinitions);
        when(gpioAdapter.provisionDigitalInputPin(any(Pin.class), any(PinPullResistance.class))).thenReturn(mock(GpioPinDigitalInput.class));

        monitorService.postConstruct();

        assertEquals(6, monitorService.sensors.size());
        assertTrue(monitorService.sensors.get(0) instanceof AirQualitySensor);
        assertTrue(monitorService.sensors.get(1) instanceof LightSensor);
        assertTrue(monitorService.sensors.get(2) instanceof RelativeHumiditySensor);
        assertTrue(monitorService.sensors.get(3) instanceof ReservoirPhSensor);
        assertTrue(monitorService.sensors.get(4) instanceof SoilSensor);
        assertTrue(monitorService.sensors.get(5) instanceof TemperatureSensor);
    }

    @Test
    void testBuildSensors() {
        SensorDefinition sensorDefinition = new SensorDefinition();
        sensorDefinition.setName("TestSensor");
        sensorDefinition.setType("temp");
        sensorDefinition.setPinAddress(1);
        sensorDefinition.setTimeout(1000L);
        List<SensorDefinition> sensorDefinitions = List.of(sensorDefinition);
        when(configProperties.getSensorDefinitions()).thenReturn(sensorDefinitions);

        List<Sensor<?>> sensors = monitorService.buildSensors();

        assertEquals(1, sensors.size());
        assertEquals("TestSensor", sensors.get(0).getName());
    }

    @Test
    void testCreateSensor() {
        GpioPinDigitalInput inputPin = mock(GpioPinDigitalInput.class);
        when(gpioAdapter.provisionDigitalInputPin(any(Pin.class), any(PinPullResistance.class))).thenReturn(inputPin);

        Sensor<?> sensor = monitorService.createSensor(TEMPERATURE, "TestSensor", 1, 1000L);

        assertNotNull(sensor);
        assertEquals("TestSensor", sensor.getName());
    }

    @Test
    void testGetSensorState() {
        @SuppressWarnings("unchecked") Sensor<Double> sensor = mock(Sensor.class);
        when(sensor.getName()).thenReturn("TestSensor");
        when(sensor.currentValue()).thenReturn(25.0);

        monitorService.sensors = List.of(sensor);

        Map<String, Object> sensorState = monitorService.getSensorState();

        assertEquals(1, sensorState.size());
        assertTrue(sensorState.containsKey("TestSensor"));
        assertEquals(25.0, sensorState.get("TestSensor"));
    }

    @Test
    void testGetSensorValue() throws MonitorService.SensorNotFoundException {
        @SuppressWarnings("unchecked") Sensor<Double> sensor = mock(Sensor.class);
        when(sensor.getName()).thenReturn("TestSensor");
        when(sensor.currentValue()).thenReturn(25.0);

        monitorService.sensors = List.of(sensor);

        Object value = monitorService.getSensorValue("TestSensor");

        assertEquals(25.0, value);
    }

    @Test
    void testPostConstruct_BuildActuators() {
        List<ActuatorDefinition> actuatorDefinitions = new ArrayList<>();
        actuatorDefinitions.add(new ActuatorDefinition("Actuator 1", 18));

        when(configProperties.getActuatorDefinitions()).thenReturn(actuatorDefinitions);
        when(gpioAdapter.provisionDigitalOutputPin(any(Pin.class), any(PinState.class))).thenReturn(mock(GpioPinDigitalOutput.class));

        monitorService.postConstruct();

        Map<String, Actuator> actuators = monitorService.buildActuators(gpioAdapter);
        assertEquals(1, actuators.size());
        assertNotNull(actuators.get("Actuator 1"));
    }
}