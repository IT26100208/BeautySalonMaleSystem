package com.WD45.BeautySalonMaleSystem.dto;

public class DashboardStatsDTO {
    private long totalCustomers;
    private long totalStaff;
    private long totalServices;
    private long totalAppointments;
    private long pendingAppointments;
    private long completedAppointments;
    private double totalRevenue;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(long totalCustomers, long totalStaff, long totalServices, long totalAppointments, long pendingAppointments, long completedAppointments, double totalRevenue) {
        this.totalCustomers = totalCustomers;
        this.totalStaff = totalStaff;
        this.totalServices = totalServices;
        this.totalAppointments = totalAppointments;
        this.pendingAppointments = pendingAppointments;
        this.completedAppointments = completedAppointments;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(long totalStaff) {
        this.totalStaff = totalStaff;
    }

    public long getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(long totalServices) {
        this.totalServices = totalServices;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public long getPendingAppointments() {
        return pendingAppointments;
    }

    public void setPendingAppointments(long pendingAppointments) {
        this.pendingAppointments = pendingAppointments;
    }

    public long getCompletedAppointments() {
        return completedAppointments;
    }

    public void setCompletedAppointments(long completedAppointments) {
        this.completedAppointments = completedAppointments;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
