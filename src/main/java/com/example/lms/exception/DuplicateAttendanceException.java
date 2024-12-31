package com.example.lms.exception;

public class DuplicateAttendanceException extends RuntimeException {
    public DuplicateAttendanceException(String message) {
        super(message);
    }

    public DuplicateAttendanceException(String message, Throwable cause) {
        super(message, cause);
    }
}