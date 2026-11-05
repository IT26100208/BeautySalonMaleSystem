package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.AppointmentDTO;
import com.WD45.BeautySalonMaleSystem.dto.AppointmentFormDTO;
import com.WD45.BeautySalonMaleSystem.dto.RescheduleDTO;
import com.WD45.BeautySalonMaleSystem.entity.Appointment;
import com.WD45.BeautySalonMaleSystem.entity.AppointmentStatus;
import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import com.WD45.BeautySalonMaleSystem.exception.AppointmentNotFoundException;
import com.WD45.BeautySalonMaleSystem.exception.InvalidAppointmentException;
import com.WD45.BeautySalonMaleSystem.repository.AppointmentRepository;
import com.WD45.BeautySalonMaleSystem.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
    static final LocalTime CLOSING_TIME = LocalTime.of(19, 0);
    static final int SLOT_MINUTES = 30;
    static final int MAX_DAYS_AHEAD = 60;

    private static final List<String> STYLISTS = List.of("Kasun", "Nimal", "Tharindu", "Ashen");

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentFileHandler fileHandler;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            AppointmentFileHandler fileHandler,
            ServiceRepository serviceRepository) {

        this.appointmentRepository = appointmentRepository;
        this.fileHandler = fileHandler;
        this.serviceRepository = serviceRepository;
    }

    // ------------------------------------------------------------------
    // Customer operations
    // ------------------------------------------------------------------


    @Override
    public AppointmentDTO bookAppointment(
            AppointmentFormDTO form,
            String customerEmail) {

        //Find the selected service
        SalonService selectedService = serviceRepository
                .findById(form.getServiceId())
                .orElseThrow(() ->
                        new InvalidAppointmentException(
                                "The selected salon service does not exist."
                        )
                );

        //Allow bookings only for active services
        if (!Boolean.TRUE.equals(selectedService.getIsActive())) {
            throw new InvalidAppointmentException(
                    "This service is currently unavailable."
            );
        }

        //Validate the current service values
        if (selectedService.getPrice() == null
                || !Double.isFinite(selectedService.getPrice())
                || selectedService.getPrice() <= 0
                || selectedService.getDurationMinutes() == null
                || selectedService.getDurationMinutes() <= 0) {

            throw new InvalidAppointmentException(
                    "This service has invalid pricing or duration."
            );
        }

        //Create Regular or Premium appointment
        Appointment appointment =
                form.getAppointmentType().createAppointment();

        appointment.setCustomerEmail(customerEmail);
        appointment.setCustomerName(
                form.getCustomerName().trim()
        );
        appointment.setPhoneNumber(
                form.getPhoneNumber().trim()
        );

        //Link the real database service
        appointment.setSalonService(selectedService);

        // Preserve duration at booking time
        appointment.setBookedDurationMinutes(
                selectedService.getDurationMinutes()
        );

        appointment.setStylist(form.getStylist());
        appointment.setAppointmentDate(form.getAppointmentDate());
        appointment.setAppointmentTime(form.getAppointmentTime());
        appointment.setNotes(blankToNull(form.getNotes()));

        //Validate date, time and stylist availability
        LocalDateTime now = now();

        validateSlot(appointment, now);

        //Save the booking
        Appointment saved =
                appointmentRepository.save(appointment);

        syncFile();

        return AppointmentDTO.from(saved, now);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getCustomerAppointments(String customerEmail) {
        return toDTOs(appointmentRepository
                .findByCustomerEmailOrderByAppointmentDateDescAppointmentTimeDesc(customerEmail));
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO getCustomerAppointment(Long id, String customerEmail) {
        return AppointmentDTO.from(findOwned(id, customerEmail), now());
    }

    @Override
    public AppointmentDTO rescheduleAppointment(Long id, RescheduleDTO dto, String customerEmail) {
        Appointment appointment = findOwned(id, customerEmail);
        LocalDateTime now = now();

        // The entity checks the cutoff rule (24h regular / 3h premium) before changing anything
        appointment.reschedule(dto.getAppointmentDate(), dto.getAppointmentTime(), dto.getStylist(), now);

        // If the new slot is invalid, the exception rolls the transaction back
        validateSlot(appointment, now);

        Appointment saved = appointmentRepository.save(appointment);
        syncFile();
        return AppointmentDTO.from(saved, now);
    }

    @Override
    public void cancelAppointment(Long id, String customerEmail) {
        Appointment appointment = findOwned(id, customerEmail);
        appointment.cancel(now());
        appointmentRepository.save(appointment);
        syncFile();
    }

    // ------------------------------------------------------------------
    // Admin operations
    // ------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments(AppointmentStatus status, LocalDate date) {
        List<Appointment> appointments;
        if (status != null && date != null) {
            appointments = appointmentRepository.findByAppointmentDateAndStatusOrderByAppointmentTimeAsc(date, status);
        } else if (date != null) {
            appointments = appointmentRepository.findByAppointmentDateOrderByAppointmentTimeAsc(date);
        } else if (status != null) {
            appointments = appointmentRepository.findByStatusOrderByAppointmentDateDescAppointmentTimeDesc(status);
        } else {
            appointments = appointmentRepository.findAllByOrderByAppointmentDateDescAppointmentTimeDesc();
        }
        return toDTOs(appointments);
    }

    @Override
    public AppointmentDTO updateStatus(Long id, AppointmentStatus newStatus) {
        Appointment appointment = findById(id);
        appointment.changeStatus(newStatus);
        Appointment saved = appointmentRepository.save(appointment);
        syncFile();
        return AppointmentDTO.from(saved, now());
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = findById(id);
        appointmentRepository.delete(appointment);
        syncFile();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<AppointmentStatus, Long> countByStatus() {
        Map<AppointmentStatus, Long> counts = new EnumMap<>(AppointmentStatus.class);
        for (AppointmentStatus status : AppointmentStatus.values()) {
            counts.put(status, appointmentRepository.countByStatus(status));
        }
        return counts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String[]> getFileRecords() {
        return fileHandler.readAll();
    }

    @Override
    public String getDataFilePath() {
        return fileHandler.getAbsolutePath();
    }

    // ------------------------------------------------------------------
    // Form options
    // ------------------------------------------------------------------

    @Override
    public List<String> getStylists() {
        return STYLISTS;
    }

    @Override
    public List<LocalTime> getTimeSlots() {
        List<LocalTime> slots = new ArrayList<>();
        for (LocalTime t = OPENING_TIME; t.isBefore(CLOSING_TIME); t = t.plusMinutes(SLOT_MINUTES)) {
            slots.add(t);
        }
        return slots;
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    /** All the rules a booking time must follow. Throws InvalidAppointmentException if one is broken. */
    void validateSlot(Appointment candidate, LocalDateTime now) {
        LocalDate date = candidate.getAppointmentDate();
        LocalTime time = candidate.getAppointmentTime();

        if (!STYLISTS.contains(candidate.getStylist())) {
            throw new InvalidAppointmentException("Please choose a stylist from the list.");
        }
        if (!candidate.getStartDateTime().isAfter(now)) {
            throw new InvalidAppointmentException("Please choose a time in the future.");
        }
        if (date.isAfter(now.toLocalDate().plusDays(MAX_DAYS_AHEAD))) {
            throw new InvalidAppointmentException("Appointments can be booked up to " + MAX_DAYS_AHEAD + " days ahead.");
        }
        if (time.isBefore(OPENING_TIME) || time.getMinute() % SLOT_MINUTES != 0 || time.getSecond() != 0) {
            throw new InvalidAppointmentException("Please choose one of the available time slots.");
        }
        LocalTime end = candidate.getEndDateTime().toLocalTime();
        if (end.isAfter(CLOSING_TIME) || !candidate.getEndDateTime().toLocalDate().equals(date)) {
            throw new InvalidAppointmentException("This service takes " + candidate.getDurationMinutes()
                    + " minutes and would end after closing time (" + CLOSING_TIME + "). Please pick an earlier slot.");
        }

        List<Appointment> sameDay = appointmentRepository.findByStylistAndAppointmentDateAndStatusIn(
                candidate.getStylist(), date, AppointmentStatus.activeStatuses());
        for (Appointment other : sameDay) {
            boolean isSelf = candidate.getId() != null && candidate.getId().equals(other.getId());
            if (!isSelf && candidate.overlapsWith(other)) {
                throw new InvalidAppointmentException(candidate.getStylist() + " is already booked from "
                        + other.getAppointmentTime() + " to " + other.getEndDateTime().toLocalTime()
                        + " on " + date + ". Please choose another time or stylist.");
            }
        }
    }

    private Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    /** Customers can only see / change their own appointments. */
    private Appointment findOwned(Long id, String customerEmail) {
        Appointment appointment = findById(id);
        if (!appointment.getCustomerEmail().equalsIgnoreCase(customerEmail)) {
            throw new AppointmentNotFoundException(id);
        }
        return appointment;
    }

    private List<AppointmentDTO> toDTOs(List<Appointment> appointments) {
        LocalDateTime now = now();
        return appointments.stream().map(a -> AppointmentDTO.from(a, now)).toList();
    }

    /** Keeps data/appointments.txt in step with the database. */
    private void syncFile() {
        fileHandler.writeAll(appointmentRepository.findAllByOrderByAppointmentDateDescAppointmentTimeDesc());
    }

    protected LocalDateTime now() {
        return LocalDateTime.now();
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
