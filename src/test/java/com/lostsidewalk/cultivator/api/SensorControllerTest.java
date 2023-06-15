package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.MonitorService;
import com.lostsidewalk.cultivator.SensorDefinition;
import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.OK;

class SensorControllerTest {

    @Mock
    private CultivatorConfigProperties configProperties;

    @Mock
    private MonitorService monitorService;

    @InjectMocks
    private SensorController sensorController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testGetSensorDefinitions() {
        List<SensorDefinition> expectedDefinitions = Arrays.asList(
                new SensorDefinition("sensor1", "Sensor 1", 0),
                new SensorDefinition("sensor2", "Sensor 2", 1)
        );

        when(configProperties.getSensorDefinitions()).thenReturn(expectedDefinitions);

        ResponseEntity<List<SensorDefinition>> response = sensorController.getSensorDefinitions();

        assertEquals(OK, response.getStatusCode());
        assertEquals(expectedDefinitions, response.getBody());

        verify(configProperties, times(1)).getSensorDefinitions();
        verifyNoMoreInteractions(configProperties);
    }

    @Test
    void testGetCurrentValue_ExistingSensor() {
        String sensorName = "sensor1";
        Object expectedValue = 25.5;

        when(monitorService.getSensorValue(sensorName)).thenReturn(expectedValue);

        ResponseEntity<?> response = sensorController.getCurrentValue(sensorName);

        assertEquals(OK, response.getStatusCode());
        assertEquals(expectedValue, response.getBody());

        verify(monitorService, times(1)).getSensorValue(sensorName);
        verifyNoMoreInteractions(monitorService);
    }

    @Test
    void testGetCurrentValue_NonexistentSensor() {
        String sensorName = "sensor3";

        when(monitorService.getSensorValue(sensorName)).thenReturn(null);

        ResponseEntity<?> response = sensorController.getCurrentValue(sensorName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(monitorService, times(1)).getSensorValue(sensorName);
        verifyNoMoreInteractions(monitorService);
    }
}
