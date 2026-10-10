package com.WD45.BeautySalonMaleSystem.repository;

import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<SalonService, Long> {
}