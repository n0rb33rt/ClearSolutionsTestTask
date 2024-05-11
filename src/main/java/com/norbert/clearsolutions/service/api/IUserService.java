package com.norbert.clearsolutions.service.api;


import com.norbert.clearsolutions.dto.UserDTO;
import com.norbert.clearsolutions.dto.UserUpdateDTO;

import java.time.LocalDate;
import java.util.List;

public interface IUserService {
    Long createUser(UserDTO userDto);
    void updateUser(UserUpdateDTO userUpdateDTO);
    void deleteUser(String userId);
    List<UserDTO> searchByBirthDateRange(LocalDate from, LocalDate to);
}
