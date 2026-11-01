package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.entity.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * File handling for appointments.
 *
 * After every create / update / delete the full list of appointments is written
 * to a text file (default: data/appointments.txt). The admin "File Records" page
 * reads the file back and shows it. One appointment per line, fields separated by "|".
 */
@Component
public class AppointmentFileHandler {

    private static final Logger log = LoggerFactory.getLogger(AppointmentFileHandler.class);

    public static final String DELIMITER = "|";
    public static final String HEADER =
            "id|type|customerName|customerEmail|phone|service|stylist|date|time|status|price|rescheduleCount";

    private final Path filePath;

    public AppointmentFileHandler(@Value("${app.appointments.file:data/appointments.txt}") String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /** Overwrites the file with the current list of appointments. */
    public void writeAll(List<Appointment> appointments) {
        try {
            Path parent = filePath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                writer.write(HEADER);
                writer.newLine();
                for (Appointment a : appointments) {
                    writer.write(toLine(a));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            // The database is the main store, so a file error must not break the booking.
            log.error("Could not write appointments file {}", filePath.toAbsolutePath(), e);
        }
    }

    /** Reads every record (header skipped). Each record is returned as an array of fields. */
    public List<String[]> readAll() {
        if (!Files.exists(filePath)) {
            return Collections.emptyList();
        }
        List<String[]> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                if (!line.isBlank()) {
                    records.add(line.split("\\|", -1));
                }
            }
        } catch (IOException e) {
            log.error("Could not read appointments file {}", filePath.toAbsolutePath(), e);
        }
        return records;
    }

    public String getAbsolutePath() {
        return filePath.toAbsolutePath().toString();
    }

    private String toLine(Appointment a) {
        return String.join(DELIMITER,
                String.valueOf(a.getId()),
                a.getTypeLabel(),
                clean(a.getCustomerName()),
                clean(a.getCustomerEmail()),
                clean(a.getPhoneNumber()),
                a.getServiceType().getDisplayName(),
                clean(a.getStylist()),
                a.getAppointmentDate().toString(),
                a.getAppointmentTime().toString(),
                a.getStatus().name(),
                a.calculateTotalPrice().toPlainString(),
                String.valueOf(a.getRescheduleCount()));
    }

    /** Removes the delimiter and line breaks so one record always stays on one line. */
    private String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(DELIMITER, "/").replace("\r", " ").replace("\n", " ");
    }
}
