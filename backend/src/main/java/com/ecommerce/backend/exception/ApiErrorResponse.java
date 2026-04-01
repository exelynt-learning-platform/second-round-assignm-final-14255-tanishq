package com.ecommerce.backend.exception;

import java.util.List;

public class ApiErrorResponse {
    private String message;
    private int status;
    private List<String> errors;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ApiErrorResponse(String message, int status, List<String> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
