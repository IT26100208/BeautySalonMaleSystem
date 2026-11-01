package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.CustomerDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;
import com.WD45.BeautySalonMaleSystem.entity.Admin;
import com.WD45.BeautySalonMaleSystem.entity.Customer;
import com.WD45.BeautySalonMaleSystem.entity.User;
import com.WD45.BeautySalonMaleSystem.exception.UserNotFoundException;
import com.WD45.BeautySalonMaleSystem.repository.UserRepository;
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

            AdminDTO dto =
                    new AdminDTO();


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
}