package com.WD45.BeautySalonMaleSystem.exception;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(Long id) {
        super("Appointment #" + id + " was not found.");
    }
}
