package com.WD45.BeautySalonMaleSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "system_settings")
public class SystemSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String salonName;

    @Column(nullable = false)
    private String openingTime;

    @Column(nullable = false)
    private String closingTime;

    private String contactPhone;
    private String contactEmail;
    private String currencySymbol;
    private Integer maxAppointmentsPerSlot;

    public SystemSettings() {}

    public SystemSettings(String salonName, String openingTime, String closingTime, String contactPhone, String contactEmail, String currencySymbol, Integer maxAppointmentsPerSlot) {
        this.salonName = salonName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.currencySymbol = currencySymbol;
        this.maxAppointmentsPerSlot = maxAppointmentsPerSlot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Integer getMaxAppointmentsPerSlot() {
        return maxAppointmentsPerSlot;
    }

    public void setMaxAppointmentsPerSlot(Integer maxAppointmentsPerSlot) {
        this.maxAppointmentsPerSlot = maxAppointmentsPerSlot;
    }
}
