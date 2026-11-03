package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.AdminDTO;
import com.WD45.BeautySalonMaleSystem.dto.CustomerDTO;
import com.WD45.BeautySalonMaleSystem.dto.PasswordChangeDTO;
import com.WD45.BeautySalonMaleSystem.dto.ProfileUpdateDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;
import com.WD45.BeautySalonMaleSystem.entity.Admin;
import com.WD45.BeautySalonMaleSystem.entity.Customer;
import com.WD45.BeautySalonMaleSystem.entity.User;
import com.WD45.BeautySalonMaleSystem.exception.UserNotFoundException;
import com.WD45.BeautySalonMaleSystem.repository.UserRepository;
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


    // READ ALL USERS

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // READ ONE USER

    @Override
    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + id
                        ));

        return convertToDTO(user);
    }


    // CREATE USER

    @Override
    public UserDTO createUser(UserFormDTO dto) {

        // Check whether email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {

            throw new IllegalArgumentException(
                    "Email already exists."
            );
        }

        User user;

        // Create ADMIN
        if ("ADMIN".equalsIgnoreCase(dto.getUserType())) {

            Admin admin = new Admin();

            admin.setEmployeeId(
                    dto.getEmployeeId()
            );

            user = admin;

        }

        // Create CUSTOMER
        else if ("CUSTOMER".equalsIgnoreCase(dto.getUserType())) {

            Customer customer = new Customer();

            customer.setAddress(
                    dto.getAddress()
            );

            customer.setLoyaltyNumber(
                    dto.getLoyaltyNumber()
            );

            user = customer;

        }

        // Invalid user type
        else {

            throw new IllegalArgumentException(
                    "Invalid user type."
            );
        }


        // Set common fields
        setCommonUserFields(user, dto);


        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );


        // Save user
        User savedUser =
                userRepository.save(user);


        return convertToDTO(savedUser);
    }

    // UPDATE USER

    @Override
    public UserDTO updateUser(
            Long id,
            UserFormDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + id
                        ));


        // Check email belongs to another user
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(existingUser -> {

                    if (!existingUser.getId().equals(id)) {

                        throw new IllegalArgumentException(
                                "Email already exists."
                        );
                    }
                });


        // Update common fields
        user.setFirstName(
                dto.getFirstName()
        );

        user.setLastName(
                dto.getLastName()
        );

        user.setEmail(
                dto.getEmail()
        );

        user.setPhoneNumber(
                dto.getPhoneNumber()
        );




        // Update password only if entered
        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            dto.getPassword()
                    )
            );
        }


        // Update ADMIN information
        if (user instanceof Admin
                && "ADMIN".equalsIgnoreCase(dto.getUserType())) {

            Admin admin = (Admin) user;

            admin.setEmployeeId(
                    dto.getEmployeeId()
            );
        }


        // Update CUSTOMER information
        if (user instanceof Customer
                && "CUSTOMER".equalsIgnoreCase(dto.getUserType())) {

            Customer customer =
                    (Customer) user;

            customer.setAddress(
                    dto.getAddress()
            );

            customer.setLoyaltyNumber(
                    dto.getLoyaltyNumber()
            );
        }


        User updatedUser =
                userRepository.save(user);


        return convertToDTO(updatedUser);
    }

    // DELETE USER

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID: " + id
                        ));

        userRepository.delete(user);
    }

    // REGISTER CUSTOMER

    @Override
    public UserDTO registerCustomer(UserFormDTO dto) {

        // Check email
        if (userRepository.existsByEmail(dto.getEmail())) {

            throw new IllegalArgumentException(
                    "An account with this email already exists."
            );
        }


        Customer customer = new Customer();


        customer.setFirstName(
                dto.getFirstName()
        );

        customer.setLastName(
                dto.getLastName()
        );

        customer.setEmail(
                dto.getEmail()
        );

        customer.setPhoneNumber(
                dto.getPhoneNumber()
        );

        customer.setAddress(
                dto.getAddress()
        );


        // Automatically generate loyalty number
        customer.setLoyaltyNumber(
                generateLoyaltyNumber()
        );


        // Encrypt password
        customer.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );




        User savedUser =
                userRepository.save(customer);


        return convertToDTO(savedUser);
    }

    // GENERATE CUSTOMER LOYALTY NUMBER

    private String generateLoyaltyNumber() {

        return "CUS" + System.currentTimeMillis();
    }

    // SET COMMON USER FIELDS

    private void setCommonUserFields(
            User user,
            UserFormDTO dto) {

        user.setFirstName(
                dto.getFirstName()
        );

        user.setLastName(
                dto.getLastName()
        );

        user.setEmail(
                dto.getEmail()
        );

        user.setPhoneNumber(
                dto.getPhoneNumber()
        );


    }

    // CONVERT ENTITY → DTO

    private UserDTO convertToDTO(User user) {


        // ADMIN
        if (user instanceof Admin) {

            Admin admin =
                    (Admin) user;

            AdminDTO dto = new AdminDTO();


            setCommonDTOFields(
                    dto,
                    user
            );


            dto.setEmployeeId(
                    admin.getEmployeeId()
            );


            return dto;
        }


        // CUSTOMER
        if (user instanceof Customer) {

            Customer customer =
                    (Customer) user;

            CustomerDTO dto =
                    new CustomerDTO();


            setCommonDTOFields(
                    dto,
                    user
            );


            dto.setAddress(
                    customer.getAddress()
            );

            dto.setLoyaltyNumber(
                    customer.getLoyaltyNumber()
            );


            return dto;
        }


        return null;
    }

    // COMMON DTO FIELDS

    private void setCommonDTOFields(
            UserDTO dto,
            User user) {

        dto.setId(
                user.getId()
        );

        dto.setFirstName(
                user.getFirstName()
        );

        dto.setLastName(
                user.getLastName()
        );

        dto.setEmail(
                user.getEmail()
        );

        dto.setPhoneNumber(
                user.getPhoneNumber()
        );


    }

    @Override
    public CustomerDTO getCurrentCustomerProfile() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // Query userRepository directly instead of calling getUserByEmail
        User user = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + currentUsername));

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(user.getId());
        customerDTO.setFirstName(user.getFirstName());
        customerDTO.setLastName(user.getLastName());
        customerDTO.setEmail(user.getEmail());
        customerDTO.setPhoneNumber(user.getPhoneNumber());

        if (user instanceof Customer customer) {
            customerDTO.setAddress(customer.getAddress());
            customerDTO.setLoyaltyNumber(customer.getLoyaltyNumber());
        }

        return customerDTO;
    }

    @Override
    public void updateCurrentCustomerProfile(UserFormDTO userFormDTO) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // Update logic for current customer profile
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void updateProfile(
            String email,
            ProfileUpdateDTO profileUpdateDTO) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Account not found."
                        )
                );

        String fullName = profileUpdateDTO.getFullName();
        String phone = profileUpdateDTO.getPhone();

        // Check the submitted values
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException(
                    "Full name is required."
            );
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException(
                    "Phone number is required."
            );
        }

        // Email cannot be changed through this form yet
        if (profileUpdateDTO.getEmail() == null ||
                !email.equalsIgnoreCase(
                        profileUpdateDTO.getEmail().trim())) {

            throw new IllegalArgumentException(
                    "Email changes are not supported here."
            );
        }

        // Separate the full name into first and last names
        String[] names = fullName.trim().split("\\s+", 2);

        if (names.length < 2) {
            throw new IllegalArgumentException(
                    "Please enter both first and last name."
            );
        }

        // Update the existing user
        user.setFirstName(names[0]);
        user.setLastName(names[1]);
        user.setPhoneNumber(phone.trim());

        // Save the changes to MySQL
        userRepository.save(user);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public boolean updatePassword(
            String email,
            PasswordChangeDTO passwordChangeDTO) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Account not found."
                        )
                );

        String current = passwordChangeDTO.getCurrentPassword();
        String next = passwordChangeDTO.getNewPassword();
        String confirmation = passwordChangeDTO.getConfirmPassword();

        // Validate submitted passwords
        if (current == null ||
                next == null ||
                confirmation == null) {
            return false;
        }

        // Verify the current password
        if (!passwordEncoder.matches(
                current, user.getPassword())) {
            return false;
        }

        // Require a password with at least 8 characters
        if (next.length() < 8) {
            return false;
        }

        // New password and confirmation must match
        if (!next.equals(confirmation)) {
            return false;
        }

        // Prevent reusing the current password
        if (passwordEncoder.matches(
                next, user.getPassword())) {
            return false;
        }

        // Hash the new password before saving.
        user.setPassword(passwordEncoder.encode(next));

        userRepository.save(user);

        return true;
    }
}