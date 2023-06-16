package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.MonitorService;
import com.lostsidewalk.cultivator.SensorDefinition;
import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class SensorControllerTest {

    @Mock
    private CultivatorConfigProperties configProperties;

    @Mock
    private MonitorService monitorService;

    private SensorController sensorController;

    @BeforeEach
    void setUp() {
        openMocks(this);
        sensorController = new SensorController();
        sensorController.configProperties = configProperties;
        sensorController.monitorService = monitorService;
    }

    @Test
    void testGetSensorDefinitions() {
        SensorDefinition sensorDefinition = new SensorDefinition();
        sensorDefinition.setName("TestSensor");
        List<SensorDefinition> sensorDefinitions = List.of(sensorDefinition);
        when(configProperties.getSensorDefinitions()).thenReturn(sensorDefinitions);

        ResponseEntity<List<SensorDefinition>> responseEntity = sensorController.getSensorDefinitions();

        assertEquals(sensorDefinitions, responseEntity.getBody());
    }

    @Test
    void testGetCurrentValues() {
        Map<String, Object> currentValues = singletonMap("TestSensor", 25.0);
        when(monitorService.getSensorState()).thenReturn(currentValues);

        ResponseEntity<Map<String, ?>> responseEntity = sensorController.getCurrentValues();

        assertEquals(currentValues, responseEntity.getBody());
    }

    @Test
    void testGetCurrentValue() throws MonitorService.SensorNotFoundException {
        when(monitorService.getSensorValue("TestSensor")).thenReturn(25.0);

        ResponseEntity<?> responseEntity = sensorController.getCurrentValue("TestSensor");

        assertEquals(25.0, responseEntity.getBody());
    }
}