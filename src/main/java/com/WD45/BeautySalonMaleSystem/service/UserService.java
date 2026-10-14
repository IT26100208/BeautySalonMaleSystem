
package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.UserDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;
import com.WD45.BeautySalonMaleSystem.dto.CustomerDTO;

import java.util.List;

public interface UserService {

    // READ ALL USERS
    List<UserDTO> getAllUsers();

    // READ ONE USER
    UserDTO getUserById(Long id);

    // CREATE USER
    UserDTO createUser(UserFormDTO user);

    // UPDATE USER
    UserDTO updateUser(Long id, UserFormDTO user);

    // DELETE USER
    void deleteUser(Long id);

    // CUSTOMER REGISTRATION
    UserDTO registerCustomer(UserFormDTO user);

    // GET LOGGED-IN CUSTOMER PROFILE
    CustomerDTO getCurrentCustomerProfile();

    // UPDATE LOGGED-IN CUSTOMER PROFILE
    CustomerDTO updateCurrentCustomerProfile(UserFormDTO user);
}
