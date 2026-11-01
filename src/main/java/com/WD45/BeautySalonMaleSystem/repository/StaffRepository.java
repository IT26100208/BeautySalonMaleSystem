package com.WD45.BeautySalonMaleSystem.repository;

import com.WD45.BeautySalonMaleSystem.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StaffRepository
        extends JpaRepository<Staff, Long> {

    List<Staff> findByNameContainingIgnoreCase(String name);

    List<Staff> findByPositionContainingIgnoreCase(
            String position
    );
}