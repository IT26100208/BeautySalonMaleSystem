
package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.AdminDTO;
import com.WD45.BeautySalonMaleSystem.dto.CustomerDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;

import com.WD45.BeautySalonMaleSystem.entity.Admin;
import com.WD45.BeautySalonMaleSystem.entity.Customer;
import com.WD45.BeautySalonMaleSystem.entity.User;

import com.WD45.BeautySalonMaleSystem.exception.UserNotFoundException;
import com.WD45.BeautySalonMaleSystem.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================================
    // READ ALL USERS
    // ==========================================

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ==========================================
    // READ ONE USER
    // ==========================================

    @Override
    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + id
                        ));

        return convertToDTO(user);
    }

    // ==========================================
    // CREATE USER
    // ==========================================

    @Override
    public UserDTO createUser(UserFormDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already exists."
            );
        }

        User user;

        if ("ADMIN".equalsIgnoreCase(dto.getUserType())) {

            Admin admin = new Admin();
            admin.setEmployeeId(dto.getEmployeeId());
            user = admin;

        } else if ("CUSTOMER".equalsIgnoreCase(dto.getUserType())) {

            Customer customer = new Customer();
            customer.setAddress(dto.getAddress());

            customer.setLoyaltyNumber(
                    dto.getLoyaltyNumber() != null
                            && !dto.getLoyaltyNumber().isBlank()
                            ? dto.getLoyaltyNumber()
                            : generateLoyaltyNumber()
            );

            user = customer;

        } else {
            throw new IllegalArgumentException("Invalid user type.");
        }

        setCommonUserFields(user, dto);

        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        return convertToDTO(userRepository.save(user));
    }

    // ==========================================
    // UPDATE USER BY ID (ADMIN MANAGEMENT)
    // ==========================================

    @Override
    public UserDTO updateUser(Long id, UserFormDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + id
                        ));

        checkEmailAvailable(dto.getEmail(), id);

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(dto.getPassword())
            );
        }

        if (user instanceof Admin admin) {
            admin.setEmployeeId(dto.getEmployeeId());
        }

        if (user instanceof Customer customer) {
            customer.setAddress(dto.getAddress());
        }

        return convertToDTO(userRepository.save(user));
    }

    // ==========================================
    // DELETE USER
    // ==========================================

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + id
                        ));

        userRepository.delete(user);
    }

    // ==========================================
    // REGISTER CUSTOMER
    // ==========================================

    @Override
    public UserDTO registerCustomer(UserFormDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "An account with this email already exists."
            );
        }

        Customer customer = new Customer();

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());

        customer.setLoyaltyNumber(generateLoyaltyNumber());

        customer.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        return convertToDTO(userRepository.save(customer));
    }

    // ==========================================
    // GET CURRENT CUSTOMER PROFILE
    // ==========================================

    @Override
    public CustomerDTO getCurrentCustomerProfile() {

        User user = getAuthenticatedUser();

        if (!(user instanceof Customer customer)) {
            throw new IllegalStateException(
                    "Only customers can access this profile."
            );
        }

        return convertCustomerToDTO(customer);
    }

    // ==========================================
    // UPDATE CURRENT CUSTOMER PROFILE
    // ==========================================

    @Override
    public CustomerDTO updateCurrentCustomerProfile(
            UserFormDTO dto) {

        User user = getAuthenticatedUser();

        if (!(user instanceof Customer customer)) {
            throw new IllegalStateException(
                    "Only customers can update this profile."
            );
        }

        checkEmailAvailable(dto.getEmail(), customer.getId());

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());

        // Only change password when the customer enters a new one.
        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {

            customer.setPassword(
                    passwordEncoder.encode(dto.getPassword())
            );
        }

        Customer savedCustomer =
                (Customer) userRepository.save(customer);

        return convertCustomerToDTO(savedCustomer);
    }

    // ==========================================
    // FIND CURRENTLY AUTHENTICATED USER
    // ==========================================

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().equals("anonymousUser")) {

            throw new IllegalStateException(
                    "You must log in to access your profile."
            );
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Logged-in user was not found."
                        ));
    }

    // ==========================================
    // CHECK EMAIL AVAILABILITY
    // ==========================================

    private void checkEmailAvailable(String email, Long currentId) {

        userRepository.findByEmail(email)
                .ifPresent(existingUser -> {

                    if (!existingUser.getId().equals(currentId)) {
                        throw new IllegalArgumentException(
                                "This email is already registered."
                        );
                    }
                });
    }

    // ==========================================
    // GENERATE CUSTOMER LOYALTY NUMBER
    // ==========================================

    private String generateLoyaltyNumber() {

        return "CUS" + System.currentTimeMillis();
    }

    // ==========================================
    // SET COMMON USER FIELDS
    // ==========================================

    private void setCommonUserFields(
            User user,
            UserFormDTO dto) {

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
    }

    // ==========================================
    // CONVERT ENTITY TO DTO
    // ==========================================

    private UserDTO convertToDTO(User user) {

        if (user instanceof Admin admin) {

            AdminDTO dto = new AdminDTO();

            setCommonDTOFields(dto, user);
            dto.setEmployeeId(admin.getEmployeeId());

            return dto;
        }

        if (user instanceof Customer customer) {
            return convertCustomerToDTO(customer);
        }

        throw new IllegalStateException(
                "Unknown user type: " + user.getClass().getName()
        );
    }

    // ==========================================
    // CONVERT CUSTOMER TO CUSTOMER DTO
    // ==========================================

    private CustomerDTO convertCustomerToDTO(Customer customer) {

        CustomerDTO dto = new CustomerDTO();

        setCommonDTOFields(dto, customer);
        dto.setAddress(customer.getAddress());
        dto.setLoyaltyNumber(customer.getLoyaltyNumber());

        return dto;
    }

    // ==========================================
    // COMMON DTO FIELDS
    // ==========================================

    private void setCommonDTOFields(
            UserDTO dto,
            User user) {

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
    }
}
