package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.MonitorService.ActuatorNotFoundException;
import com.lostsidewalk.cultivator.MonitorService.SensorNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();

    @Test
    void testHandleActuatorNotFoundException() {
        ActuatorNotFoundException exception = new ActuatorNotFoundException("Actuator not found");
        MockHttpServletRequest request = new MockHttpServletRequest();
        WebRequest webRequest = new ServletWebRequest(request);
        ResponseEntity<?> responseEntity = exceptionHandler.handleActuatorNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testHandleSensorNotFoundException() {
        SensorNotFoundException exception = new SensorNotFoundException("Sensor not found");
        MockHttpServletRequest request = new MockHttpServletRequest();
        WebRequest webRequest = new ServletWebRequest(request);
        ResponseEntity<?> responseEntity = exceptionHandler.handleActuatorNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
