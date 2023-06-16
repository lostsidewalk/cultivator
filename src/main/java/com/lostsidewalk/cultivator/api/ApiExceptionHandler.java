package com.lostsidewalk.cultivator.api;

import com.lostsidewalk.cultivator.MonitorService.ActuatorNotFoundException;
import com.lostsidewalk.cultivator.MonitorService.SensorNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ActuatorNotFoundException.class)
    protected ResponseEntity<?> handleActuatorNotFoundException(ActuatorNotFoundException ex, WebRequest request) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SensorNotFoundException.class)
    protected ResponseEntity<?> handleActuatorNotFoundException(SensorNotFoundException ex, WebRequest request) {
        return ResponseEntity.notFound().build();
    }
}
