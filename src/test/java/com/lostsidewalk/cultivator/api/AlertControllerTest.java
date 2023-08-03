package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.Alert.AlertState;
import com.lostsidewalk.cultivator.AlertDefinition;
import com.lostsidewalk.cultivator.MonitorService;
import com.lostsidewalk.cultivator.MonitorService.AlertNotFoundException;
import com.lostsidewalk.cultivator.app.CultivatorConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.lostsidewalk.cultivator.Alert.AlertState.WARN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AlertControllerTest {

    @Mock
    private CultivatorConfigProperties configProperties;

    @Mock
    private MonitorService monitorService;

    private AlertController alertController;

    @BeforeEach
    void setUp() {
        openMocks(this);
        alertController = new AlertController();
        alertController.configProperties = configProperties;
        alertController.monitorService = monitorService;
    }

    @Test
    void testGetAlertDefinitions() {
        List<AlertDefinition> expectedDefinitions = Arrays.asList(
                new AlertDefinition("Alert1"),
                new AlertDefinition("Alert2")
        );
        when(configProperties.getAlertDefinitions()).thenReturn(expectedDefinitions);

        ResponseEntity<List<AlertDefinition>> response = alertController.getAlertDefinitions();

        assertEquals(expectedDefinitions, response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(configProperties).getAlertDefinitions();
    }

    @Test
    void testGetCurrentState() throws AlertNotFoundException {
        String alertName = "Alert1";
        AlertState expectedState = WARN;
        when(monitorService.getAlertState(alertName)).thenReturn(expectedState);

        ResponseEntity<?> response = alertController.getCurrentState(alertName);

        assertEquals(expectedState, response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(monitorService).getAlertState(alertName);
    }
}
