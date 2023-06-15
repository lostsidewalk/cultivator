package com.lostsidewalk.cultivator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * This file is part of the Cultivator project.
 * (c) [2023] [Lost Sidewalk Software LLC]
 *
 * SPDX-License-Identifier: MIT
 */
@ExtendWith(MockitoExtension.class)
public class RulesEngineTest {

    @Mock
    private KieContainer kieContainer;

    @Mock
    private KieSession kieSession;

    @InjectMocks
    private RulesEngine rulesEngine;

    @BeforeEach
    public void setup() {
        when(kieContainer.newKieSession()).thenReturn(kieSession);
    }

    @Test
    public void evaluateRules_shouldFireAllRulesAndDisposeSession() {
        // Prepare test data
        Map<String, Object> sensorState = new HashMap<>();
        sensorState.put("testSensor", 1);
        Map<String, Actuator> actuators = new HashMap<>();
        actuators.put("testActuator", mock(Actuator.class));

        // Call the method under test
        rulesEngine.evaluateRules(sensorState, actuators);

        // Verify that the session was created and used
        verify(kieContainer).newKieSession();
        verify(kieSession).insert(sensorState);
        verify(kieSession).insert(actuators);
        verify(kieSession).fireAllRules();
        verify(kieSession).dispose();
    }
}
