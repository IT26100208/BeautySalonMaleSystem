package com.WD45.BeautySalonMaleSystem.entity;

import java.math.BigDecimal;

/**
 * Salon services that can be booked. Prices are in LKR.
 * (If a teammate builds a Service Management module later, this enum can be
 * replaced by their Service entity.)
 */
public enum ServiceType {

    HAIRCUT("Haircut", "1500", 30),
    BEARD_TRIM("Beard Trim & Shape", "800", 20),
    HOT_TOWEL_SHAVE("Hot Towel Shave", "1000", 30),
    HAIRCUT_BEARD_COMBO("Haircut + Beard Combo", "2000", 45),
    HAIR_COLOURING("Hair Colouring", "3500", 60),
    FACIAL("Men's Facial", "3000", 45),
    HEAD_MASSAGE("Head & Shoulder Massage", "1200", 20);

    private final String displayName;
    private final BigDecimal basePrice;
    private final int durationMinutes;

    ServiceType(String displayName, String basePrice, int durationMinutes) {
        this.displayName = displayName;
        this.basePrice = new BigDecimal(basePrice);
        this.durationMinutes = durationMinutes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
