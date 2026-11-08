package com.WD45.BeautySalonMaleSystem.service;
import com.WD45.BeautySalonMaleSystem.repository.ServiceRepository;
import com.WD45.BeautySalonMaleSystem.dto.DashboardStatsDTO;
import com.WD45.BeautySalonMaleSystem.entity.Appointment;
import com.WD45.BeautySalonMaleSystem.entity.AppointmentStatus;
import com.WD45.BeautySalonMaleSystem.entity.Customer;
import com.WD45.BeautySalonMaleSystem.entity.SystemSettings;
import com.WD45.BeautySalonMaleSystem.repository.AppointmentRepository;
import com.WD45.BeautySalonMaleSystem.repository.SettingsRepository;
import com.WD45.BeautySalonMaleSystem.repository.StaffRepository;
import com.WD45.BeautySalonMaleSystem.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final SettingsRepository settingsRepository;

    public DashboardServiceImpl(
            UserRepository userRepository,
            StaffRepository staffRepository,
            ServiceRepository serviceRepository,
            AppointmentRepository appointmentRepository,
            SettingsRepository settingsRepository) {

        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {

        // Count customers
        long totalCustomers = userRepository.findAll()
                .stream()
                .filter(user -> user instanceof Customer)
                .count();

        // Count staff and salon services
        long totalStaff = staffRepository.count();
        long totalServices = serviceRepository.count();

        // Read actual appointment statistics
        long totalAppointments = appointmentRepository.count();

        long pendingAppointments =
                appointmentRepository.countByStatus(
                        AppointmentStatus.PENDING);

        long completedAppointments =
                appointmentRepository.countByStatus(
                        AppointmentStatus.COMPLETED);

        // Estimated revenue from completed appointments
        double totalRevenue = appointmentRepository.findByStatusOrderByAppointmentDateDescAppointmentTimeDesc(
                        AppointmentStatus.COMPLETED)
                .stream()
                .map(Appointment::calculateTotalPrice)
                .filter(price -> price != null)
                .mapToDouble(price -> price.doubleValue())
                .sum();

        return new DashboardStatsDTO(
                totalCustomers,
                totalStaff,
                totalServices,
                totalAppointments,
                pendingAppointments,
                completedAppointments,
                totalRevenue
        );
    }

    @Override
    @Transactional
    public SystemSettings getSystemSettings() {

        return settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> {

                    SystemSettings defaultSettings =
                            new SystemSettings(
                                    "Bella Aura",
                                    "09:00",
                                    "20:00",
                                    "+94 11 234 5678",
                                    "admin@salon.lk",
                                    "LKR",
                                    5
                            );

                    return settingsRepository.save(defaultSettings);
                });
    }

    @Override
    @Transactional
    public SystemSettings saveSystemSettings(SystemSettings settings) {

        SystemSettings existing = getSystemSettings();

        existing.setSalonName(settings.getSalonName());
        existing.setOpeningTime(settings.getOpeningTime());
        existing.setClosingTime(settings.getClosingTime());
        existing.setContactPhone(settings.getContactPhone());
        existing.setContactEmail(settings.getContactEmail());
        existing.setCurrencySymbol(settings.getCurrencySymbol());
        existing.setMaxAppointmentsPerSlot(
                settings.getMaxAppointmentsPerSlot());

        return settingsRepository.save(existing);
    }
}
