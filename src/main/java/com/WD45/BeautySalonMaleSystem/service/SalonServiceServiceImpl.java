package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.ServiceDTO;
import com.WD45.BeautySalonMaleSystem.dto.ServiceFormDTO;
import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import com.WD45.BeautySalonMaleSystem.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.WD45.BeautySalonMaleSystem.exception.ServiceNotFoundException;

import java.util.List;

@Service
public class SalonServiceServiceImpl implements SalonServiceService {

    private final ServiceRepository serviceRepository;

    public SalonServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<ServiceDTO> getAllActiveServices() {
        return serviceRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ServiceDTO getServiceById(Long id) {
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new ServiceNotFoundException("Service not found with ID: " + id));

        return convertToDTO(service);
    }

    @Override
    @Transactional
    public ServiceDTO createService(ServiceFormDTO formDTO) {
        String name = formDTO.getName().trim();

        if (serviceRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException(
                    "A service named '" + name + "' already exists.");
        }

        SalonService service = new SalonService();
        service.setName(name);
        service.setCategory(formDTO.getCategory().trim());
        service.setDescription(
                formDTO.getDescription() == null ? "" : formDTO.getDescription().trim());
        service.setPrice(formDTO.getPrice());
        service.setDurationMinutes(formDTO.getDurationMinutes());
        service.setIsActive(formDTO.getIsActive() != null ? formDTO.getIsActive() : true);

        return convertToDTO(serviceRepository.save(service));
    }

    @Override
    @Transactional
    public ServiceDTO updateService(Long id, ServiceFormDTO formDTO) {
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new ServiceNotFoundException("Service not found with ID: " + id));

        String name = formDTO.getName().trim();

        if (serviceRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new IllegalArgumentException(
                    "Another service already exists with the name: " + name);
        }

        service.setName(name);
        service.setCategory(formDTO.getCategory().trim());
        service.setDescription(
                formDTO.getDescription() == null ? "" : formDTO.getDescription().trim());
        service.setPrice(formDTO.getPrice());
        service.setDurationMinutes(formDTO.getDurationMinutes());

        if (formDTO.getIsActive() != null) {
            service.setIsActive(formDTO.getIsActive());
        }

        return convertToDTO(serviceRepository.save(service));
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new ServiceNotFoundException("Service not found with ID: " + id);
        }

        serviceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleServiceStatus(Long id) {
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new ServiceNotFoundException("Service not found with ID: " + id));

        service.setIsActive(!Boolean.TRUE.equals(service.getIsActive()));
        serviceRepository.save(service);
    }

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

    @Override
    public List<ServiceDTO> getServicesByCategory(String category) {
        if (category == null
                || category.trim().isEmpty()
                || "All".equalsIgnoreCase(category.trim())) {
            return getAllActiveServices();
        }

        return serviceRepository.findByCategoryAndIsActiveTrue(category.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ServiceDTO> getAllServicesByCategory(String category) {
        if (category == null
                || category.trim().isEmpty()
                || "All".equalsIgnoreCase(category.trim())) {
            return getAllServices();
        }

        return serviceRepository.findByCategoryIgnoreCase(category.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<String> getAllCategories() {
        return serviceRepository.findDistinctCategories();
    }

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