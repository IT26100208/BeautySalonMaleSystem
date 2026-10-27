package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.AppointmentDTO;
import com.WD45.BeautySalonMaleSystem.dto.AppointmentFormDTO;
import com.WD45.BeautySalonMaleSystem.dto.RescheduleDTO;
import com.WD45.BeautySalonMaleSystem.entity.AppointmentType;
import com.WD45.BeautySalonMaleSystem.entity.ServiceType;
import com.WD45.BeautySalonMaleSystem.exception.AppointmentNotFoundException;
import com.WD45.BeautySalonMaleSystem.exception.InvalidAppointmentException;
import com.WD45.BeautySalonMaleSystem.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

/** Customer side: Book, View, Reschedule and Cancel appointments. */
@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private static final String REDIRECT_LIST = "redirect:/appointments";

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // ---------------- View ----------------

    @GetMapping
    public String myAppointments(Principal principal, Model model) {
        model.addAttribute("appointments", appointmentService.getCustomerAppointments(principal.getName()));
        return "appointment/list";
    }

    // ---------------- Book ----------------

    @GetMapping("/book")
    public String bookForm(Model model) {
        model.addAttribute("form", new AppointmentFormDTO());
        addBookingOptions(model);
        return "appointment/book";
    }

    @PostMapping("/book")
    public String book(@Valid @ModelAttribute("form") AppointmentFormDTO form,
                       BindingResult result,
                       Principal principal,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            addBookingOptions(model);
            return "appointment/book";
        }
        try {
            AppointmentDTO booked = appointmentService.bookAppointment(form, principal.getName());
            redirect.addFlashAttribute("success", "Appointment #" + booked.getId() + " booked for "
                    + booked.getAppointmentDate() + " at " + booked.getAppointmentTime() + ". Status: Pending.");
            return REDIRECT_LIST;
        } catch (InvalidAppointmentException e) {
            model.addAttribute("error", e.getMessage());
            addBookingOptions(model);
            return "appointment/book";
        }
    }

    // ---------------- Reschedule ----------------

    @GetMapping("/{id}/reschedule")
    public String rescheduleForm(@PathVariable Long id, Principal principal,
                                 Model model, RedirectAttributes redirect) {
        try {
            AppointmentDTO appointment = appointmentService.getCustomerAppointment(id, principal.getName());
            if (!appointment.isCanReschedule()) {
                redirect.addFlashAttribute("error", appointment.getTypeLabel()
                        + " appointments can only be rescheduled while active and at least "
                        + appointment.getRescheduleCutoffHours() + " hours before the start time.");
                return REDIRECT_LIST;
            }
            model.addAttribute("appointment", appointment);
            model.addAttribute("form", new RescheduleDTO(
                    appointment.getAppointmentDate(), appointment.getAppointmentTime(), appointment.getStylist()));
            addRescheduleOptions(model);
            return "appointment/reschedule";
        } catch (AppointmentNotFoundException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return REDIRECT_LIST;
        }
    }

    @PostMapping("/{id}/reschedule")
    public String reschedule(@PathVariable Long id,
                             @Valid @ModelAttribute("form") RescheduleDTO form,
                             BindingResult result,
                             Principal principal,
                             Model model,
                             RedirectAttributes redirect) {
        try {
            if (result.hasErrors()) {
                return showRescheduleAgain(id, principal, model, null);
            }
            AppointmentDTO updated = appointmentService.rescheduleAppointment(id, form, principal.getName());
            redirect.addFlashAttribute("success", "Appointment #" + id + " moved to "
                    + updated.getAppointmentDate() + " at " + updated.getAppointmentTime()
                    + " with " + updated.getStylist() + ". Waiting for confirmation.");
            return REDIRECT_LIST;
        } catch (InvalidAppointmentException e) {
            return showRescheduleAgain(id, principal, model, e.getMessage());
        } catch (AppointmentNotFoundException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return REDIRECT_LIST;
        }
    }

    // ---------------- Cancel ----------------

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, Principal principal, RedirectAttributes redirect) {
        try {
            appointmentService.cancelAppointment(id, principal.getName());
            redirect.addFlashAttribute("success", "Appointment #" + id + " was cancelled.");
        } catch (InvalidAppointmentException | AppointmentNotFoundException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_LIST;
    }

    // ---------------- Helpers ----------------

    private String showRescheduleAgain(Long id, Principal principal, Model model, String error) {
        model.addAttribute("appointment", appointmentService.getCustomerAppointment(id, principal.getName()));
        if (error != null) {
            model.addAttribute("error", error);
        }
        addRescheduleOptions(model);
        return "appointment/reschedule";
    }

    private void addBookingOptions(Model model) {
        model.addAttribute("services", ServiceType.values());
        model.addAttribute("types", AppointmentType.values());
        addRescheduleOptions(model);
    }

    private void addRescheduleOptions(Model model) {
        model.addAttribute("stylists", appointmentService.getStylists());
        model.addAttribute("timeSlots", appointmentService.getTimeSlots());
        model.addAttribute("minDate", LocalDate.now());
        model.addAttribute("maxDate", LocalDate.now().plusDays(60));
    }
}
