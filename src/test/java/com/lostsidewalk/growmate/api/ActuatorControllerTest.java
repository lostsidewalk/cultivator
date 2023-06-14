package com.lostsidewalk.growmate.api;

import com.lostsidewalk.growmate.ActuatorDefinition;
import com.lostsidewalk.growmate.MonitorService;
import com.lostsidewalk.growmate.app.GrowMateConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ActuatorControllerTest {

    @Mock
    private GrowMateConfigProperties configProperties;

    @Mock
    private MonitorService monitorService;

    private ActuatorController actuatorController;

    @BeforeEach
    void setUp() {
        openMocks(this);
        actuatorController = new ActuatorController();
        actuatorController.configProperties = configProperties;
        actuatorController.monitorService = monitorService;
    }

    @Test
    void getActuatorDefinitions() {
        List<ActuatorDefinition> expectedDefinitions = Collections.singletonList(
                new ActuatorDefinition("actuator1", 1)
        );

        when(configProperties.getActuatorDefinitions()).thenReturn(expectedDefinitions);

        ResponseEntity<List<ActuatorDefinition>> response = actuatorController.getActuatorDefinitions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDefinitions, response.getBody());

        verify(configProperties, times(1)).getActuatorDefinitions();
        verifyNoMoreInteractions(configProperties);
    }

    @Test
    void updateActuator() {
        String actuatorName = "actuator1";

        ResponseEntity<?> response = actuatorController.updateActuator(actuatorName);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(monitorService, times(1)).toggleActuator(actuatorName);
        verifyNoMoreInteractions(monitorService);
    }
}
