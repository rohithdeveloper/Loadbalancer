package com.example.employee.exceptioncontroller;

public class ServiceUnavailableException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ServiceUnavailableException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
}
