package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.DashboardStatsDTO;
import com.WD45.BeautySalonMaleSystem.entity.SystemSettings;

public interface DashboardService {
    DashboardStatsDTO getDashboardStats();
    SystemSettings getSystemSettings();
    SystemSettings saveSystemSettings(SystemSettings settings);
}
