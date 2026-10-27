package com.WD45.BeautySalonMaleSystem.dto;

import com.WD45.BeautySalonMaleSystem.entity.Appointment;
import com.WD45.BeautySalonMaleSystem.entity.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Read-only view of an appointment for the pages.
 * Keeps the entity out of the templates and pre-computes the values the UI needs.
 */
public class AppointmentDTO {

    private final Long id;
    private final String customerName;
    private final String customerEmail;
    private final String phoneNumber;
    private final String serviceName;
    private final String typeLabel;
    private final String stylist;
    private final LocalDate appointmentDate;
    private final LocalTime appointmentTime;
    private final LocalTime endTime;
    private final int durationMinutes;
    private final BigDecimal totalPrice;
    private final AppointmentStatus status;
    private final List<AppointmentStatus> nextStatuses;
    private final String notes;
    private final int rescheduleCount;
    private final int rescheduleCutoffHours;
    private final boolean canReschedule;
    private final boolean canCancel;
    private final LocalDateTime createdAt;

    private AppointmentDTO(Appointment a, LocalDateTime now) {
        this.id = a.getId();
        this.customerName = a.getCustomerName();
        this.customerEmail = a.getCustomerEmail();
        this.phoneNumber = a.getPhoneNumber();
        this.serviceName = a.getServiceType().getDisplayName();
        this.typeLabel = a.getTypeLabel();
        this.stylist = a.getStylist();
        this.appointmentDate = a.getAppointmentDate();
        this.appointmentTime = a.getAppointmentTime();
        this.endTime = a.getEndDateTime().toLocalTime();
        this.durationMinutes = a.getDurationMinutes();
        this.totalPrice = a.calculateTotalPrice();
        this.status = a.getStatus();
        this.nextStatuses = List.copyOf(a.getStatus().nextStatuses());
        this.notes = a.getNotes();
        this.rescheduleCount = a.getRescheduleCount();
        this.rescheduleCutoffHours = a.getRescheduleCutoffHours();
        this.canReschedule = a.canBeRescheduled(now);
        this.canCancel = a.canBeCancelled(now);
        this.createdAt = a.getCreatedAt();
    }

    public static AppointmentDTO from(Appointment appointment, LocalDateTime now) {
        return new AppointmentDTO(appointment, now);
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public String getStylist() {
        return stylist;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public List<AppointmentStatus> getNextStatuses() {
        return nextStatuses;
    }

    public String getNotes() {
        return notes;
    }

    public int getRescheduleCount() {
        return rescheduleCount;
    }

    public int getRescheduleCutoffHours() {
        return rescheduleCutoffHours;
    }

    public boolean isCanReschedule() {
        return canReschedule;
    }

    public boolean isCanCancel() {
        return canCancel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
