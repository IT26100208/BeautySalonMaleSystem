package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import com.WD45.BeautySalonMaleSystem.exception.ServiceNotFoundException;
import com.WD45.BeautySalonMaleSystem.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalonServiceServiceImpl implements SalonServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<SalonService> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public SalonService getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
    }

    @Override
    public SalonService saveService(SalonService service) {
        return serviceRepository.save(service);
    }

    @Override
    public SalonService updateService(Long id, SalonService serviceDetails) {
        SalonService existingService = getServiceById(id);
        existingService.setName(serviceDetails.getName());
        existingService.setDescription(serviceDetails.getDescription());
        existingService.setPrice(serviceDetails.getPrice());
        existingService.setDurationMinutes(serviceDetails.getDurationMinutes());
        return serviceRepository.save(existingService);
    }

    @Override
    public void deleteService(Long id) {
        SalonService service = getServiceById(id);
        serviceRepository.delete(service);
    }
}