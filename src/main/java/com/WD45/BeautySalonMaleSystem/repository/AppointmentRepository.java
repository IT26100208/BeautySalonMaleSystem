package com.WD45.BeautySalonMaleSystem.repository;

import com.WD45.BeautySalonMaleSystem.entity.Appointment;
import com.WD45.BeautySalonMaleSystem.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomerEmailOrderByAppointmentDateDescAppointmentTimeDesc(String customerEmail);

    List<Appointment> findAllByOrderByAppointmentDateDescAppointmentTimeDesc();

    List<Appointment> findByStatusOrderByAppointmentDateDescAppointmentTimeDesc(AppointmentStatus status);

    List<Appointment> findByAppointmentDateOrderByAppointmentTimeAsc(LocalDate date);

    List<Appointment> findByAppointmentDateAndStatusOrderByAppointmentTimeAsc(LocalDate date, AppointmentStatus status);

    /** Active bookings of one stylist on one day - used to block double bookings. */
    List<Appointment> findByStylistAndAppointmentDateAndStatusIn(
            String stylist, LocalDate date, Collection<AppointmentStatus> statuses);

    long countByStatus(AppointmentStatus status);
}
