package com.norbert.clearsolutions.service.impl;

import com.norbert.clearsolutions.dto.UserDTO;
import com.norbert.clearsolutions.dto.UserUpdateDTO;
import com.norbert.clearsolutions.entity.User;
import com.norbert.clearsolutions.exception.BadRequestException;
import com.norbert.clearsolutions.exception.UserNotFoundException;
import com.norbert.clearsolutions.mapper.UserDTOMapper;
import com.norbert.clearsolutions.repository.UserRepository;
import com.norbert.clearsolutions.service.api.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    @Value(value = "${constants.min-user-age}")
    private Integer minUserAge;


    @Override
    public Long createUser(UserDTO userDTO) {
        if(!isUserAdult(userDTO.getBirthDate())){
            throw new BadRequestException("Invalid birth date. You should have 18 years old");
        }
        if (existsByEmail(userDTO.getEmail())) {
            throw new BadRequestException("The email is already taken");
        }
        if (userDTO.getPhone() != null && existsByPhone(userDTO.getPhone())) {
            throw new BadRequestException("The phone is already taken");
        }
        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .birthDate(userDTO.getBirthDate())
                .phone(userDTO.getPhone())
                .address(userDTO.getAddress())
                .build();
        return userRepository.save(user).getId();
    }

    private boolean isUserAdult(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        LocalDate adultAge = now.minusYears(minUserAge);
        return birthDate.isBefore(adultAge);
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }


    @Override
    public void updateUser(UserUpdateDTO userDTO) {
        Long id = userDTO.getId();
        if (!existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new IllegalStateException("User database is not working"));

        validateUserDetails(userDTO, user);

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setBirthDate(userDTO.getBirthDate());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());

        userRepository.save(user);
    }

    private void validateUserDetails(UserUpdateDTO userDTO, User user) {
        if (!isUserAdult(userDTO.getBirthDate())) {
            throw new BadRequestException("Invalid birth date");
        }

        String email = userDTO.getEmail();
        if (existsByEmail(email) && !email.equals(user.getEmail())) {
            throw new BadRequestException("The email is already taken");
        }

        String phone = userDTO.getPhone();
        if (phone != null && existsByPhone(phone) && !phone.equals(user.getPhone())) {
            throw new BadRequestException("The phone is already used");
        }
    }


    @Override
    public void deleteUser(String userIdStr) {
        Long id = parseToLong(userIdStr);

        if(!existsById(id)){
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private Long parseToLong(String value){
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid number format: " + value);
        }
    }

    private boolean existsById(Long id){
        return userRepository.existsById(id);
    }

    @Override
    public List<UserDTO> searchByBirthDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BadRequestException("Invalid birth date range");
        }
        return userRepository
                .findAll()
                .stream()
                .filter(user ->
                        (from.isEqual(user.getBirthDate()) || from.isBefore(user.getBirthDate()))
                                && (to.isEqual(user.getBirthDate()) || to.isAfter(user.getBirthDate()))
                )
                .map(userDTOMapper)
                .toList();
    }

}
