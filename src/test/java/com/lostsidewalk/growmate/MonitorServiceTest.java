package com.lostsidewalk.growmate;

import com.lostsidewalk.growmate.app.GpioAdapter;
import com.lostsidewalk.growmate.app.GrowMateConfigProperties;
import com.lostsidewalk.growmate.sensors.*;
import com.pi4j.io.gpio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lostsidewalk.growmate.MonitorService.SensorType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * This file is part of the GrowMate project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
class MonitorServiceTest {

    @Mock
    private GrowMateConfigProperties configProperties;

    @Mock
    private GpioAdapter gpioAdapter;

    @InjectMocks
    private MonitorService monitorService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        monitorService.sensors = new ArrayList<>();
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
    void testGetSensorState() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        when(temperatureSensor.getName()).thenReturn("Temperature Sensor");
        when(temperatureSensor.currentValue()).thenReturn(new BigDecimal(25));

        monitorService.sensors.add(temperatureSensor);

        Map<String, Object> expectedSensorState = new HashMap<>();
        expectedSensorState.put("Temperature Sensor", new BigDecimal(25));

        Map<String, Object> sensorState = monitorService.getSensorState();

        assertEquals(expectedSensorState, sensorState);
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
