package com.WD45.BeautySalonMaleSystem.repository;

import com.WD45.BeautySalonMaleSystem.entity.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<SystemSettings, Long> {
}
