package com.ecommerce.backend.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(HttpServletRequest request) {
        Object statusCodeAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (statusCodeAttr instanceof Number number) {
            statusCode = number.intValue();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Something went wrong");
        response.put("status", statusCode);

        return ResponseEntity.status(statusCode).body(response);
    }
}
