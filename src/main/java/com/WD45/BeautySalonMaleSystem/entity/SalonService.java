package com.WD45.BeautySalonMaleSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "services")
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salon_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalonService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Default Constructor
    public SalonService() {
    }

    // Parameterized Constructor
    public SalonService(String name, String category, String description, Double price, Integer durationMinutes) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.isActive = true;
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
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Helper / OOP business methods
    public String getFormattedPrice() {
        if (price == null) {
            return "LKR 0.00";
        }
        return String.format("LKR %,.2f", price);
    }

    public String getFormattedDuration() {
        if (durationMinutes == null) {
            return "0 mins";
        }
        return durationMinutes + " mins";
    }
}
