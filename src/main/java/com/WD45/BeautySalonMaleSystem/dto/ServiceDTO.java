package com.WD45.BeautySalonMaleSystem.dto;

public class ServiceDTO {

    private Long id;
    private String name;
    private String category;
    private String description;
    private Double price;
    private Integer durationMinutes;
    private Boolean isActive;
    private String formattedPrice;
    private String formattedDuration;

    public ServiceDTO() {
    }

    public ServiceDTO(Long id, String name, String category, String description, Double price, Integer durationMinutes, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.isActive = isActive;
        this.formattedPrice = price != null ? String.format("LKR %,.2f", price) : "LKR 0.00";
        this.formattedDuration = durationMinutes != null ? durationMinutes + " mins" : "0 mins";
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        this.formattedPrice = price != null ? String.format("LKR %,.2f", price) : "LKR 0.00";
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        this.formattedDuration = durationMinutes != null ? durationMinutes + " mins" : "0 mins";
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getFormattedDuration() {
        return formattedDuration;
    }

    public void setFormattedDuration(String formattedDuration) {
        this.formattedDuration = formattedDuration;
    }
//
}
