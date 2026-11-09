package com.WD45.BeautySalonMaleSystem.service;


import com.WD45.BeautySalonMaleSystem.dto.*;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO createUser(UserFormDTO user);

    UserDTO updateUser(Long id, UserFormDTO user);

    void deleteUser(Long id);

    UserDTO registerCustomer(UserFormDTO user);

    CustomerDTO getCurrentCustomerProfile();

    void updateCurrentCustomerProfile(UserFormDTO userFormDTO);

    void updateProfile(String email, ProfileUpdateDTO profileUpdateDTO);

    boolean updatePassword(String email, PasswordChangeDTO passwordChangeDTO);
}