package com.WD45.BeautySalonMaleSystem.repository;

import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<SalonService, Long> {

    // Find all active services for customers / booking
    List<SalonService> findByIsActiveTrue();

    // Find active services by category
    List<SalonService> findByCategoryAndIsActiveTrue(String category);

    // Search active services by keyword in name or description
    @Query("SELECT s FROM SalonService s WHERE s.isActive = true AND " +
           "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<SalonService> searchActiveServices(@Param("keyword") String keyword);

    // Search all services for Admin (active + inactive)
    @Query("SELECT s FROM SalonService s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SalonService> searchAllServices(@Param("keyword") String keyword);

    // Find distinct categories
    @Query("SELECT DISTINCT s.category FROM SalonService s WHERE s.isActive = true ORDER BY s.category ASC")
    List<String> findDistinctCategories();

    // Check duplicate name
    boolean existsByNameIgnoreCase(String name);

    // Check duplicate name excluding current ID (for edit/update)
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<SalonService, Long> {
}
