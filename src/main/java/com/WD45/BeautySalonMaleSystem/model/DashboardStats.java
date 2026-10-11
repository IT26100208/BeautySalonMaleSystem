package com.WD45.BeautySalonMaleSystem.model;

public class DashboardStats {
    private int totalBookings;
    private double totalRevenue;
    private int pendingBookings;
    private int totalServices;

    public DashboardStats(int totalBookings, double totalRevenue, int pendingBookings, int totalServices) {
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
        this.pendingBookings = pendingBookings;
        this.totalServices = totalServices;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getPendingBookings() {
        return pendingBookings;
    }

    public int getTotalServices() {
        return totalServices;
    }
}
