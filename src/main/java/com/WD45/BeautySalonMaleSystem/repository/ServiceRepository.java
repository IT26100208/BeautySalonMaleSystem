package com.WD45.BeautySalonMaleSystem.repository;

import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<SalonService, Long> {

    List<SalonService> findByIsActiveTrue();

    List<SalonService> findByCategoryAndIsActiveTrue(String category);

    List<SalonService> findByCategoryIgnoreCase(String category);

    @Query("SELECT s FROM SalonService s WHERE s.isActive = true AND " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<SalonService> searchActiveServices(@Param("keyword") String keyword);

    @Query("SELECT s FROM SalonService s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SalonService> searchAllServices(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT s.category FROM SalonService s " +
            "WHERE s.isActive = true ORDER BY s.category ASC")
    List<String> findDistinctCategories();

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}