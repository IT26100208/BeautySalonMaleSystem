package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import java.util.List;

public interface SalonServiceService {
    List<SalonService> getAllServices();
    SalonService getServiceById(Long id);
    SalonService saveService(SalonService service);
    SalonService updateService(Long id, SalonService serviceDetails);
    void deleteService(Long id);
}