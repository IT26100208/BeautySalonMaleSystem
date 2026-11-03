package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.entity.AppointmentStatus;
import com.WD45.BeautySalonMaleSystem.exception.AppointmentNotFoundException;
import com.WD45.BeautySalonMaleSystem.exception.InvalidAppointmentException;
import com.WD45.BeautySalonMaleSystem.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/** Admin side: view every appointment, change Status, delete, and see the file records. */
@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private static final String REDIRECT_LIST = "redirect:/admin/appointments";

    private final AppointmentService appointmentService;

    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String allAppointments(@RequestParam(required = false) AppointmentStatus status,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments(status, date));
        model.addAttribute("counts", appointmentService.countByStatus());
        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDate", date);
        return "admin/appointments";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam AppointmentStatus status,
                               RedirectAttributes redirect) {
        try {
            appointmentService.updateStatus(id, status);
            redirect.addFlashAttribute("success", "Appointment #" + id + " is now " + status.getLabel() + ".");
        } catch (InvalidAppointmentException | AppointmentNotFoundException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_LIST;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            appointmentService.deleteAppointment(id);
            redirect.addFlashAttribute("success", "Appointment #" + id + " was deleted.");
        } catch (AppointmentNotFoundException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/file")
    public String fileRecords(Model model) {
        model.addAttribute("records", appointmentService.getFileRecords());
        model.addAttribute("filePath", appointmentService.getDataFilePath());
        return "admin/appointment-file";
    }
}
