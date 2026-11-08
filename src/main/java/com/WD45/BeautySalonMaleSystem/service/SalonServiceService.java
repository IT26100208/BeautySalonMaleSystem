package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.ServiceDTO;
import com.WD45.BeautySalonMaleSystem.dto.ServiceFormDTO;

import java.util.List;

public interface SalonServiceService {

    List<ServiceDTO> getAllActiveServices();

    List<ServiceDTO> getAllServices();

    ServiceDTO getServiceById(Long id);

    ServiceDTO createService(ServiceFormDTO serviceFormDTO);

    ServiceDTO updateService(Long id, ServiceFormDTO serviceFormDTO);

    void deleteService(Long id);

    void toggleServiceStatus(Long id);

    List<ServiceDTO> searchActiveServices(String keyword);

    List<ServiceDTO> searchAllServices(String keyword);

    List<ServiceDTO> getServicesByCategory(String category);

    List<ServiceDTO> getAllServicesByCategory(String category);

    List<String> getAllCategories();
}