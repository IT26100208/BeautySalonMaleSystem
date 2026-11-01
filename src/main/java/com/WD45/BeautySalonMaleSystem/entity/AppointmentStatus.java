package com.WD45.BeautySalonMaleSystem.entity;

import java.util.EnumSet;
import java.util.Set;

/**
 * Life-cycle of an appointment.
 *
 *   PENDING ──► CONFIRMED ──► COMPLETED
 *      │            │
 *      │            ├──► NO_SHOW
 *      ▼            ▼
 *   CANCELLED ◄─────┘
 */
public enum AppointmentStatus {

    PENDING("Pending", "status-pending"),
    CONFIRMED("Confirmed", "status-confirmed"),
    COMPLETED("Completed", "status-completed"),
    CANCELLED("Cancelled", "status-cancelled"),
    NO_SHOW("No Show", "status-noshow");

    private final String label;
    private final String cssClass;

    AppointmentStatus(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public String getCssClass() {
        return cssClass;
    }

    /** Active appointments still hold a time slot with a stylist. */
    public boolean isActive() {
        return this == PENDING || this == CONFIRMED;
    }

    /** Statuses this status is allowed to move to. */
    public Set<AppointmentStatus> nextStatuses() {
        return switch (this) {
            case PENDING -> EnumSet.of(CONFIRMED, CANCELLED);
            case CONFIRMED -> EnumSet.of(COMPLETED, CANCELLED, NO_SHOW);
            case COMPLETED, CANCELLED, NO_SHOW -> EnumSet.noneOf(AppointmentStatus.class);
        };
    }

    public boolean canMoveTo(AppointmentStatus target) {
        return nextStatuses().contains(target);
    }

    public static Set<AppointmentStatus> activeStatuses() {
        return EnumSet.of(PENDING, CONFIRMED);
    }
}
