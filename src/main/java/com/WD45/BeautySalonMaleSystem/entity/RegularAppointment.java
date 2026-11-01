package com.WD45.BeautySalonMaleSystem.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/** Standard booking: base price, reschedule up to 24 hours before. */
@Entity
@DiscriminatorValue("REGULAR")
public class RegularAppointment extends Appointment {

    private static final int RESCHEDULE_CUTOFF_HOURS = 24;

    public RegularAppointment() {
        super();
    }

    @Override
    public BigDecimal calculateTotalPrice() {
        return getServiceType().getBasePrice();
    }

    @Override
    public int getRescheduleCutoffHours() {
        return RESCHEDULE_CUTOFF_HOURS;
    }

    @Override
    public String getTypeLabel() {
        return AppointmentType.REGULAR.getLabel();
    }
}
