package com.WD45.BeautySalonMaleSystem.model;

public class DashboardStats {
    private long totalAppointments;
    private long pendingAppointments;
    private long totalCustomers;
    private long totalStaff;
    private double totalRevenue;

    public DashboardStats() {}

    public DashboardStats(long totalAppointments, long pendingAppointments, long totalCustomers, long totalStaff, double totalRevenue) {
        this.totalAppointments = totalAppointments;
        this.pendingAppointments = pendingAppointments;
        this.totalCustomers = totalCustomers;
        this.totalStaff = totalStaff;
        this.totalRevenue = totalRevenue;
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

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}