package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.AppointmentDTO;
import com.WD45.BeautySalonMaleSystem.dto.AppointmentFormDTO;
import com.WD45.BeautySalonMaleSystem.dto.RescheduleDTO;
import com.WD45.BeautySalonMaleSystem.entity.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Appointment management operations (abstraction: controllers depend on this
 * interface, not on the implementation).
 */
public interface AppointmentService {

    // ---- Customer ----

    /** Book (Create) */
    AppointmentDTO bookAppointment(AppointmentFormDTO form, String customerEmail);

    /** View (Read) - the logged-in customer's own appointments */
    List<AppointmentDTO> getCustomerAppointments(String customerEmail);

    AppointmentDTO getCustomerAppointment(Long id, String customerEmail);

    /** Reschedule (Update) */
    AppointmentDTO rescheduleAppointment(Long id, RescheduleDTO dto, String customerEmail);

    /** Cancel (status -> CANCELLED) */
    void cancelAppointment(Long id, String customerEmail);

    // ---- Admin ----

    /** View all, optionally filtered by status and/or date */
    List<AppointmentDTO> getAllAppointments(AppointmentStatus status, LocalDate date);

    /** Status (Update) - confirm / complete / no-show / cancel */
    AppointmentDTO updateStatus(Long id, AppointmentStatus newStatus);

    /** Delete - permanently remove a record */
    void deleteAppointment(Long id);

    Map<AppointmentStatus, Long> countByStatus();

    List<String[]> getFileRecords();

    String getDataFilePath();

    // ---- Form options ----

    List<String> getStylists();

    List<LocalTime> getTimeSlots();
}
