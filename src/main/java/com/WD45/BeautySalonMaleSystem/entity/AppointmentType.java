package com.WD45.BeautySalonMaleSystem.entity;

/**
 * Booking package chosen by the customer. Each type knows how to create
 * its own Appointment subclass (simple factory).
 */
public enum AppointmentType {

    REGULAR("Regular", "Standard service. Reschedule up to 24 hours before."),
    PREMIUM("Premium", "Senior stylist, +15 min consultation, 30% extra. Reschedule up to 3 hours before.");

    private final String label;
    private final String description;

    AppointmentType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Appointment createAppointment() {
        return switch (this) {
            case REGULAR -> new RegularAppointment();
            case PREMIUM -> new PremiumAppointment();
        };
    }
}
