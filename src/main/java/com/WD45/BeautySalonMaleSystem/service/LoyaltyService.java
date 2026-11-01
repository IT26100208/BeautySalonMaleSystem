package com.WD45.BeautySalonMaleSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.WD45.BeautySalonMaleSystem.entity.Customer;
import com.WD45.BeautySalonMaleSystem.entity.LoyaltyTransaction;
import com.WD45.BeautySalonMaleSystem.repository.LoyaltyTransactionRepository;
import com.WD45.BeautySalonMaleSystem.repository.UserRepository;

@Service
public class LoyaltyService {

    private final LoyaltyTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public LoyaltyService(
            LoyaltyTransactionRepository transactionRepository,
            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public long getPointsBalance(Customer customer) {
        return transactionRepository.sumPointsByCustomerId(customer.getId());
    }

    public List<LoyaltyTransaction> getHistory(Customer customer) {
        return transactionRepository.findByCustomer_IdOrderByCreatedAtDescIdDesc(customer.getId());
    }

    @Transactional
    public LoyaltyTransaction awardPoints(Customer customer, int points, String description) {
        if (points <= 0) {
            throw new IllegalArgumentException("Awarded points must be greater than zero.");
        }
        return recordTransaction(lockCustomer(customer), points, description);
    }

    @Transactional
    public LoyaltyTransaction redeemPoints(Customer customer, int points, String description) {
        if (points <= 0) {
            throw new IllegalArgumentException("Redeemed points must be greater than zero.");
        }
        Customer lockedCustomer = lockCustomer(customer);
        if (getPointsBalance(lockedCustomer) < points) {
            throw new IllegalArgumentException("The customer does not have enough loyalty points.");
        }
        return recordTransaction(lockedCustomer, -points, description);
    }

    private Customer lockCustomer(Customer customer) {
        return userRepository.findCustomerByIdForUpdate(customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Customer account no longer exists."));
    }

    private LoyaltyTransaction recordTransaction(Customer customer, int points, String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("A transaction description is required.");
        }

        LoyaltyTransaction transaction = new LoyaltyTransaction();
        transaction.setCustomer(customer);
        transaction.setPoints(points);
        transaction.setDescription(description.trim());
        return transactionRepository.save(transaction);
    }
}
