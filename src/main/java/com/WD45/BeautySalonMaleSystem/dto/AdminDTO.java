package com.WD45.BeautySalonMaleSystem.dto;

public class AdminDTO extends UserDTO {

    private String employeeId;

    public AdminDTO() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}