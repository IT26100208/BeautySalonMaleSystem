package com.WD45.BeautySalonMaleSystem.service;


import com.WD45.BeautySalonMaleSystem.dto.UserDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;

import java.util.List;

public interface UserService {

    // READ ALL
    List<UserDTO> getAllUsers();

    // READ ONE
    UserDTO getUserById(Long id);

    // CREATE
    UserDTO createUser(UserFormDTO user);

    // UPDATE
    UserDTO updateUser(Long id, UserFormDTO user);

    // DELETE
    void deleteUser(Long id);

    // CUSTOMER REGISTRATION
    UserDTO registerCustomer(UserFormDTO user);
}