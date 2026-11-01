package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.DashboardStatsDTO;
import com.WD45.BeautySalonMaleSystem.entity.SystemSettings;
import com.WD45.BeautySalonMaleSystem.repository.ServiceRepository;
import com.WD45.BeautySalonMaleSystem.repository.SettingsRepository;
import com.WD45.BeautySalonMaleSystem.repository.StaffRepository;
import com.WD45.BeautySalonMaleSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;
    private final SettingsRepository settingsRepository;

    @Autowired
    public DashboardServiceImpl(UserRepository userRepository, StaffRepository staffRepository, ServiceRepository serviceRepository, SettingsRepository settingsRepository) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.serviceRepository = serviceRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        long totalCustomers = userRepository.count();
        long totalStaff = staffRepository.count();
        long totalServices = serviceRepository.count();

        long totalAppointments = 0;
        long pendingAppointments = 0;
        long completedAppointments = 0;
        double totalRevenue = 0.0;

        return new DashboardStatsDTO(totalCustomers, totalStaff, totalServices, totalAppointments, pendingAppointments, completedAppointments,totalRevenue);
    }

    @Override
    public SystemSettings getSystemSettings() {
        return settingsRepository.findAll().stream().findFirst().orElseGet(() ->{
            SystemSettings defaultSettings = new SystemSettings("Gentlemen's Salon","09.00", "20.00", "+94 11 234 5678", "info@gentlemensalon.lk", "LKR", 5);
            return settingsRepository.save(defaultSettings);
        });
    }

    @Override
    public SystemSettings saveSystemSettings(SystemSettings settings) {
        SystemSettings existing = getSystemSettings();
        existing.setSalonName(settings.getSalonName());
        existing.setOpeningTime(settings.getOpeningTime());
        existing.setClosingTime(settings.getClosingTime());
        existing.setContactPhone(settings.getContactPhone());
        existing.setContactEmail(settings.getContactEmail());
        existing.setCurrencySymbol(settings.getCurrencySymbol());
        existing.setMaxAppointmentsPerSlot(settings.getMaxAppointmentsPerSlot());
        return settingsRepository.save(existing);
    }
}
