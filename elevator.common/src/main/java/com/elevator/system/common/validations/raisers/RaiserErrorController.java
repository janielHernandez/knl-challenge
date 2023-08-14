package com.elevator.system.common.validations.raisers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public abstract class RaiserErrorController {
    public ResponseEntity<Map<String, String>> raiseError(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
                err -> errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }
}
