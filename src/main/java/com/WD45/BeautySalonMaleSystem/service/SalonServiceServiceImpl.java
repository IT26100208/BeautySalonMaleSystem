package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.ServiceDTO;
import com.WD45.BeautySalonMaleSystem.dto.ServiceFormDTO;
import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import com.WD45.BeautySalonMaleSystem.exception.ServiceNotFoundException;
import com.WD45.BeautySalonMaleSystem.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import com.WD45.BeautySalonMaleSystem.exception.ServiceNotFoundException;
import com.WD45.BeautySalonMaleSystem.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalonServiceServiceImpl implements SalonServiceService {

    private final ServiceRepository serviceRepository;

    public SalonServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // READ ALL ACTIVE SERVICES
    @Override
    public List<ServiceDTO> getAllActiveServices() {
        return serviceRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // READ ALL SERVICES (ADMIN)
    @Override
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // READ ONE (SERVICE DETAILS)
    @Override
    public ServiceDTO getServiceById(Long id) {
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with ID: " + id));
        return convertToDTO(service);
    }

    // CREATE (ADD SERVICE)
    @Override
    @Transactional
    public ServiceDTO createService(ServiceFormDTO formDTO) {
        if (serviceRepository.existsByNameIgnoreCase(formDTO.getName().trim())) {
            throw new IllegalArgumentException("A service named '" + formDTO.getName().trim() + "' already exists.");
        }

        SalonService service = new SalonService();
        service.setName(formDTO.getName().trim());
        service.setCategory(formDTO.getCategory().trim());
        service.setDescription(formDTO.getDescription() != null ? formDTO.getDescription().trim() : "");
        service.setPrice(formDTO.getPrice());
        service.setDurationMinutes(formDTO.getDurationMinutes());
        service.setIsActive(formDTO.getIsActive() != null ? formDTO.getIsActive() : true);

        SalonService saved = serviceRepository.save(service);
        return convertToDTO(saved);
    }

    // UPDATE (EDIT SERVICE)
    @Override
    @Transactional
    public ServiceDTO updateService(Long id, ServiceFormDTO formDTO) {
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with ID: " + id));

        if (serviceRepository.existsByNameIgnoreCaseAndIdNot(formDTO.getName().trim(), id)) {
            throw new IllegalArgumentException("Another service already exists with the name: " + formDTO.getName().trim());
        }

        service.setName(formDTO.getName().trim());
        service.setCategory(formDTO.getCategory().trim());
        service.setDescription(formDTO.getDescription() != null ? formDTO.getDescription().trim() : "");
        service.setPrice(formDTO.getPrice());
        service.setDurationMinutes(formDTO.getDurationMinutes());
        if (formDTO.getIsActive() != null) {
            service.setIsActive(formDTO.getIsActive());
        }

        SalonService updated = serviceRepository.save(service);
        return convertToDTO(updated);
    }

    // DELETE SERVICE
    @Override
    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new ServiceNotFoundException("Service not found with ID: " + id);
        }
        serviceRepository.deleteById(id);
    }

    // TOGGLE ACTIVE STATUS
    @Override
    @Transactional
    public void toggleServiceStatus(Long id) {
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with ID: " + id));
        service.setIsActive(!service.getIsActive());
        serviceRepository.save(service);
    }

    // SEARCH ACTIVE SERVICES
    @Override
    public List<ServiceDTO> searchActiveServices(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActiveServices();
        }
        return serviceRepository.searchActiveServices(keyword.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // SEARCH ALL SERVICES (ADMIN)
    @Override
    public List<ServiceDTO> searchAllServices(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllServices();
        }
        return serviceRepository.searchAllServices(keyword.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // FILTER BY CATEGORY
    @Override
    public List<ServiceDTO> getServicesByCategory(String category) {
        if (category == null || category.trim().isEmpty() || "All".equalsIgnoreCase(category)) {
            return getAllActiveServices();
        }
        return serviceRepository.findByCategoryAndIsActiveTrue(category.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // GET ALL DISTINCT CATEGORIES
    @Override
    public List<String> getAllCategories() {
        return serviceRepository.findDistinctCategories();
    }

    // HELPER: Convert Entity to DTO
    private ServiceDTO convertToDTO(SalonService service) {
        return new ServiceDTO(
                service.getId(),
                service.getName(),
                service.getCategory(),
                service.getDescription(),
                service.getPrice(),
                service.getDurationMinutes(),
                service.getIsActive()
        );
    }
}
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
