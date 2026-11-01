package com.WD45.BeautySalonMaleSystem.repository;


import com.WD45.BeautySalonMaleSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import com.WD45.BeautySalonMaleSystem.entity.Customer;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select customer from Customer customer where customer.id = :customerId")
    Optional<Customer> findCustomerByIdForUpdate(@Param("customerId") Long customerId);
}