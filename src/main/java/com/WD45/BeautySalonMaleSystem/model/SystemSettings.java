package com.WD45.BeautySalonMaleSystem.model;

public class SystemSettings {
    private String salonName;
    private String openingHours;
    private String contactEmail;
    private double taxRate;

    public SystemSettings(String salonName, String openingHours, String contactEmail, double taxRate) {
        this.salonName = salonName;
        this.openingHours = openingHours;
        this.contactEmail = contactEmail;
        this.taxRate = taxRate;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }
}
