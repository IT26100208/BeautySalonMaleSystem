package com.WD45.BeautySalonMaleSystem.entity;

import com.WD45.BeautySalonMaleSystem.exception.InvalidAppointmentException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Base class for every appointment.
 *
 * OOP concepts:
 *  - Abstraction   : abstract class with abstract methods; subclasses decide price/rules.
 *  - Encapsulation : all fields are private; state only changes through methods
 *                    (reschedule, cancel, changeStatus) that enforce the business rules.
 *  - Inheritance   : RegularAppointment and PremiumAppointment extend this class
 *                    (stored in one table using a discriminator column).
 *  - Polymorphism  : calculateTotalPrice(), getRescheduleCutoffHours(),
 *                    getDurationMinutes() and getTypeLabel() behave differently per subclass.
 */
@Entity
@Table(name = "appointments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "appointment_type", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_email", nullable = false, length = 100)
    private String customerEmail;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", length = 40)
    private ServiceType serviceType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private SalonService salonService;

    @Column(name = "finalized_price", precision = 12, scale = 2)
    private BigDecimal finalizedPrice;

    @Column(name = "booked_duration_minutes")
    private Integer bookedDurationMinutes;

    @Column(name = "stylist", nullable = false, length = 60)
    private String stylist;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @Column(name = "notes", length = 255)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(name = "reschedule_count", nullable = false)
    private int rescheduleCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Appointment() {
        // required by JPA
    }

    // ------------------------------------------------------------------
    // Abstract (polymorphic) behaviour - implemented by subclasses
    // ------------------------------------------------------------------

    /** Final price the customer pays. */
    public abstract BigDecimal calculateTotalPrice();

    /** How many hours before the start time the customer may still reschedule. */
    public abstract int getRescheduleCutoffHours();

    /** Short label shown in the UI, e.g. "Regular" or "Premium". */
    public abstract String getTypeLabel();

    /** Total time the stylist is booked for. Subclasses may add extra time. */
    public int getDurationMinutes() {

        if (bookedDurationMinutes != null) {
            return bookedDurationMinutes;
        }

        if (salonService != null) {
            return salonService.getDurationMinutes();
        }

        if (serviceType != null) {
            return serviceType.getDurationMinutes();
        }

        throw new IllegalStateException(
                "Appointment duration is unavailable."
        );
    }

    // ------------------------------------------------------------------
    // Business rules
    // ------------------------------------------------------------------

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(appointmentDate, appointmentTime);
    }

    public LocalDateTime getEndDateTime() {
        return getStartDateTime().plusMinutes(getDurationMinutes());
    }

    /** True if this appointment's time range overlaps another one's. */
    public boolean overlapsWith(Appointment other) {
        return getStartDateTime().isBefore(other.getEndDateTime())
                && other.getStartDateTime().isBefore(getEndDateTime());
    }

    public boolean canBeRescheduled(LocalDateTime now) {
        return status.isActive()
                && now.plusHours(getRescheduleCutoffHours()).isBefore(getStartDateTime());
    }

    public boolean canBeCancelled(LocalDateTime now) {
        return status.isActive() && now.isBefore(getStartDateTime());
    }

    /** Move the appointment to a new slot. A rescheduled booking goes back to PENDING. */
    public void reschedule(LocalDate newDate, LocalTime newTime, String newStylist, LocalDateTime now) {
        if (!canBeRescheduled(now)) {
            throw new InvalidAppointmentException(
                    getTypeLabel() + " appointments can only be rescheduled up to "
                            + getRescheduleCutoffHours() + " hours before the start time.");
        }
        this.appointmentDate = newDate;
        this.appointmentTime = newTime;
        this.stylist = newStylist;
        this.rescheduleCount++;
        this.status = AppointmentStatus.PENDING;
    }

    public void cancel(LocalDateTime now) {
        if (!canBeCancelled(now)) {
            throw new InvalidAppointmentException("This appointment can no longer be cancelled.");
        }
        this.status = AppointmentStatus.CANCELLED;
    }

    /** Used by the admin to confirm / complete / mark no-show / cancel. */
    public void changeStatus(AppointmentStatus newStatus) {

        if (!status.canMoveTo(newStatus)) {
            throw new InvalidAppointmentException(
                    "Cannot change status from "
                            + status.getLabel()
                            + " to "
                            + newStatus.getLabel()
                            + "."
            );
        }

        // Freeze the price at the moment of completion.
        if (newStatus == AppointmentStatus.COMPLETED
                && finalizedPrice == null) {

            finalizedPrice = calculateTotalPrice();
        }

        this.status = newStatus;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ------------------------------------------------------------------
    // Getters and setters (encapsulation)
    // status and rescheduleCount have no public setter: they only change
    // through the business methods above.
    // ------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getStylist() {
        return stylist;
    }

    public void setStylist(String stylist) {
        this.stylist = stylist;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public int getRescheduleCount() {
        return rescheduleCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SalonService getSalonService() {
        return salonService;
    }

    public void setSalonService(SalonService salonService) {
        this.salonService = salonService;
    }

    public BigDecimal getFinalizedPrice() {
        return finalizedPrice;
    }

    public Integer getBookedDurationMinutes() {
        return bookedDurationMinutes;
    }

    public void setBookedDurationMinutes(Integer minutes) {
        this.bookedDurationMinutes = minutes;
    }


    public String getServiceName() {

        if (salonService != null) {
            return salonService.getName();
        }

        if (serviceType != null) {
            return serviceType.getDisplayName();
        }

        return "Unknown Service";
    }


    protected BigDecimal getCurrentBasePrice() {

        if (salonService != null) {

            Double price = salonService.getPrice();

            if (price == null || !Double.isFinite(price)
                    || price < 0) {
                throw new IllegalStateException(
                        "Service has an invalid price."
                );
            }

            return BigDecimal.valueOf(price);
        }

        if (serviceType != null) {
            return serviceType.getBasePrice();
        }

        throw new IllegalStateException(
                "No service is associated with this appointment."
        );
    }


}
