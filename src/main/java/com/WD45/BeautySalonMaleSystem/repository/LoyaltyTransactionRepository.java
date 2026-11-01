package com.WD45.BeautySalonMaleSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.WD45.BeautySalonMaleSystem.entity.LoyaltyTransaction;

public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {

    @Query("select coalesce(sum(t.points), 0) from LoyaltyTransaction t where t.customer.id = :customerId")
    Long sumPointsByCustomerId(@Param("customerId") Long customerId);

    List<LoyaltyTransaction> findByCustomer_IdOrderByCreatedAtDescIdDesc(Long customerId);
}
