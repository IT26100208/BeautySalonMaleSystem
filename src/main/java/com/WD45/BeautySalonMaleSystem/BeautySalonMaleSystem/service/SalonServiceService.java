package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.ServiceDTO;
import com.WD45.BeautySalonMaleSystem.dto.ServiceFormDTO;

import java.util.List;

public interface SalonServiceService {

    // READ ALL ACTIVE (For Customers / Public Booking)
    List<ServiceDTO> getAllActiveServices();

    // READ ALL (For Admin Dashboard)
    List<ServiceDTO> getAllServices();

    // READ ONE BY ID (Service details)
    ServiceDTO getServiceById(Long id);

    // CREATE (Add service)
    ServiceDTO createService(ServiceFormDTO serviceFormDTO);

    // UPDATE (Edit service)
    ServiceDTO updateService(Long id, ServiceFormDTO serviceFormDTO);

    // DELETE (Delete service)
    void deleteService(Long id);

    // TOGGLE STATUS (Activate / Deactivate)
    void toggleServiceStatus(Long id);

    // SEARCH ACTIVE SERVICES
    List<ServiceDTO> searchActiveServices(String keyword);

    // SEARCH ALL SERVICES (Admin)
    List<ServiceDTO> searchAllServices(String keyword);

    // FILTER BY CATEGORY
    List<ServiceDTO> getServicesByCategory(String category);

    // GET ALL CATEGORIES
    List<String> getAllCategories();
}
