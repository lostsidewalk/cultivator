package com.lostsidewalk.cultivator.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class RuleControllerTest {

    @Mock
    private KieContainer kieContainer;

    @Mock
    private KieSession kieSession;

    @Mock
    private KieBase kieBase;

    @Mock
    private KiePackage kiePackage;

    @Mock
    private Rule rule;

    @InjectMocks
    private RuleController ruleController;

    @Test
    public void testGetRules() {
        // Mock the KIE session and packages
        when(kieContainer.newKieSession()).thenReturn(kieSession);
        when(kieSession.getKieBase()).thenReturn(kieBase);
        when(kieBase.getKiePackages()).thenReturn(List.of(kiePackage));

        // Mock the rules
        when(kiePackage.getRules()).thenReturn(List.of(rule));
        when(rule.getName()).thenReturn("Rule1");

        // Call the getRules() method
        ResponseEntity<List<String>> response = ruleController.getRules();

        // Verify the response
        assertEquals(OK, response.getStatusCode());
        List<String> expectedRules = new ArrayList<>();
        expectedRules.add("Rule1");
        assertEquals(expectedRules, response.getBody());

        // Verify that the KIE session was disposed
        verify(kieSession, times(1)).dispose();
    }
}
