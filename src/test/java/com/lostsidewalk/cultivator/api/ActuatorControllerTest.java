package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.ActuatorDefinition;
import com.lostsidewalk.cultivator.MonitorService;
import com.lostsidewalk.cultivator.MonitorService.ActuatorNotFoundException;
import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ActuatorControllerTest {

    @Mock
    private CultivatorConfigProperties configProperties;

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
    void testGetActuatorDefinitions() {
        List<ActuatorDefinition> expectedDefinitions = Arrays.asList(
                new ActuatorDefinition("Actuator1", 1),
                new ActuatorDefinition("Actuator2", 2)
        );
        when(configProperties.getActuatorDefinitions()).thenReturn(expectedDefinitions);

        ResponseEntity<List<ActuatorDefinition>> response = actuatorController.getActuatorDefinitions();

        assertEquals(expectedDefinitions, response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(configProperties).getActuatorDefinitions();
    }

    @Test
    void testGetCurrentState() throws ActuatorNotFoundException {
        String actuatorName = "Actuator1";
        Boolean expectedState = true;
        when(monitorService.getActuatorState(actuatorName)).thenReturn(expectedState);

        ResponseEntity<?> response = actuatorController.getCurrentState(actuatorName);

        assertEquals(expectedState, response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(monitorService).getActuatorState(actuatorName);
    }

    @Test
    void testEnableActuator() {
        String actuatorName = "Actuator1";

        ResponseEntity<?> response = actuatorController.enableActuator(actuatorName);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(monitorService).enableActuator(actuatorName);
    }

    @Test
    void testDisableActuator() {
        String actuatorName = "Actuator1";

        ResponseEntity<?> response = actuatorController.disableActuator(actuatorName);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(monitorService).disableActuator(actuatorName);
    }
}
