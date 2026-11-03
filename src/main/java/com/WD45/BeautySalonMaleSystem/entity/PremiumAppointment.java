package com.WD45.BeautySalonMaleSystem.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Premium booking: senior stylist and a 15-minute consultation.
 * Costs 30% more, but can be rescheduled up to 3 hours before.
 */
@Entity
@DiscriminatorValue("PREMIUM")
public class PremiumAppointment extends Appointment {

    private static final BigDecimal PREMIUM_RATE = new BigDecimal("1.30");
    private static final int CONSULTATION_MINUTES = 15;
    private static final int RESCHEDULE_CUTOFF_HOURS = 3;

    public PremiumAppointment() {
        super();
    }

    @Override
    public BigDecimal calculateTotalPrice() {
        return getServiceType().getBasePrice()
                .multiply(PREMIUM_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int getRescheduleCutoffHours() {
        return RESCHEDULE_CUTOFF_HOURS;
    }

    @Override
    public int getDurationMinutes() {
        return super.getDurationMinutes() + CONSULTATION_MINUTES;
    }

    @Override
    public String getTypeLabel() {
        return AppointmentType.PREMIUM.getLabel();
    }
}
