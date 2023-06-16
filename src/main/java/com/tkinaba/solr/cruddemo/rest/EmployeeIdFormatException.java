package com.tkinaba.solr.cruddemo.rest;

public class EmployeeIdFormatException extends RuntimeException {
    public EmployeeIdFormatException(Throwable cause) {
        super(cause);
    }

    public EmployeeIdFormatException(String message) {
        super(message);
    }

    public EmployeeIdFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
