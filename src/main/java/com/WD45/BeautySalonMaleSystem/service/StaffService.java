package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.entity.Staff;
import com.WD45.BeautySalonMaleSystem.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private final StaffRepository repository;

    public StaffService(StaffRepository repository) {
        this.repository = repository;
    }

    public List<Staff> getAllStaff() {
        return repository.findAll();
    }

    public Staff getStaffById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Staff not found"));
    }

    public Staff saveStaff(Staff staff) {
        return repository.save(staff);
    }

    public void deleteStaff(Long id) {
        repository.deleteById(id);
    }

    public List<Staff> searchStaff(String name) {
        return repository
                .findByNameContainingIgnoreCase(name);
    }
}