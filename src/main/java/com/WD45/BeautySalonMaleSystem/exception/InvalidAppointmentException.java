package com.WD45.BeautySalonMaleSystem.exception;

/** Thrown when a booking breaks a business rule (slot taken, too late to reschedule, ...). */
public class InvalidAppointmentException extends RuntimeException {

    public InvalidAppointmentException(String message) {
        super(message);
    }
}
