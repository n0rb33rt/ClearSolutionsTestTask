package com.norbert.clearsolutions.service;

import com.norbert.clearsolutions.dto.UserDTO;
import com.norbert.clearsolutions.dto.UserUpdateDTO;
import com.norbert.clearsolutions.entity.User;
import com.norbert.clearsolutions.exception.BadRequestException;
import com.norbert.clearsolutions.exception.UserNotFoundException;
import com.norbert.clearsolutions.mapper.UserDTOMapper;
import com.norbert.clearsolutions.repository.UserRepository;
import com.norbert.clearsolutions.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDTOMapper userDTOMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private User user;
    private UserUpdateDTO userUpdateDTO;

    private Integer minAge = 18;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "minUserAge", minAge);
        userDTO = new UserDTO(null, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "+380123456789");
        user = new User(1L, "John", "Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), "123 Main St", "+380123456789");
        userUpdateDTO = new UserUpdateDTO(1L, "john.update@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "456 New St", "+380987654321");
    }

    @Test
    void createUser_ShouldThrowException_IfNotAdult() {
        userDTO.setBirthDate(LocalDate.now());
        assertThrows(BadRequestException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void createUser_ShouldThrowException_IfEmailTaken() {
        Mockito.when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void createUser_ShouldThrowException_IfPhoneTaken() {
        Mockito.when(userRepository.existsByPhone(userDTO.getPhone())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.createUser(userDTO));
    }
    @Test
    void createUser_PhoneIsNull_ShouldNotThrowException() {
        userDTO.setPhone(null);
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        assertDoesNotThrow(() -> userService.createUser(userDTO));
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_SuccessfulCreation() {
        Mockito.when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByPhone(userDTO.getPhone())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Long id = userService.createUser(userDTO);
        assertEquals(user.getId(), id);
    }



    @Test
    void updateUser_EmailAlreadyTaken_ThrowsException() {
        Mockito.when(userRepository.existsById(userUpdateDTO.getId())).thenReturn(true);
        Mockito.when(userRepository.findById(userUpdateDTO.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByEmail(userUpdateDTO.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.updateUser(userUpdateDTO));
    }

    @Test
    void updateUser_PhoneAlreadyTaken_ThrowsException() {
        Mockito.when(userRepository.existsById(userUpdateDTO.getId())).thenReturn(true);
        Mockito.when(userRepository.findById(userUpdateDTO.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByPhone(userUpdateDTO.getPhone())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.updateUser(userUpdateDTO));
    }

    @Test
    void updateUser_ValidUpdate_NoException() {
        Mockito.when(userRepository.existsById(userUpdateDTO.getId())).thenReturn(true);
        Mockito.when(userRepository.findById(userUpdateDTO.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByEmail(userUpdateDTO.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByPhone(userUpdateDTO.getPhone())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.updateUser(userUpdateDTO));
        Mockito.verify(userRepository).save(any(User.class));
    }
    @Test
    void validateUserDetails_ThrowsException_IfNotAdult() {
        userUpdateDTO.setBirthDate(LocalDate.now().minusYears(minAge - 1));
        assertThrows(BadRequestException.class, () ->
                ReflectionTestUtils.invokeMethod(userService, "validateUserDetails", userUpdateDTO, user));
    }

    @Test
    void validateUserDetails_Success_IfValidInput() {
        Mockito.when(userRepository.existsByEmail(userUpdateDTO.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByPhone(userUpdateDTO.getPhone())).thenReturn(false);

        assertDoesNotThrow(() ->
                ReflectionTestUtils.invokeMethod(userService, "validateUserDetails", userUpdateDTO, user));
    }

    @Test
    void validateUserDetails_NoException_IfEmailAlreadyTaken_But_CurrentUserEmail() {
        userUpdateDTO.setEmail(user.getEmail());
        Mockito.when(userRepository.existsByEmail(userUpdateDTO.getEmail())).thenReturn(true);
        assertDoesNotThrow(() ->
                ReflectionTestUtils.invokeMethod(userService, "validateUserDetails", userUpdateDTO, user));
    }

    @Test
    void validateUserDetails_ThrowsException_IfPhoneAlreadyTaken_And_NotCurrentUserPhone() {
        userUpdateDTO.setPhone("new.phone.number");
        Mockito.when(userRepository.existsByPhone(userUpdateDTO.getPhone())).thenReturn(true);
        user.setPhone("old.phone.number");
        assertThrows(BadRequestException.class, () ->
                ReflectionTestUtils.invokeMethod(userService, "validateUserDetails", userUpdateDTO, user));
    }

    @Test
    void validateUserDetails_NoException_IfPhoneAlreadyTaken_But_CurrentUserPhone() {
        userUpdateDTO.setPhone(user.getPhone());
        Mockito.when(userRepository.existsByPhone(userUpdateDTO.getPhone())).thenReturn(true);
        assertDoesNotThrow(() ->
                ReflectionTestUtils.invokeMethod(userService, "validateUserDetails", userUpdateDTO, user));
    }

    @Test
    void validateUserDetails_ThrowsException_IfPhoneNotNull_And_AlreadyTaken_And_NotCurrentUserPhone() {
        userUpdateDTO.setPhone("new.phone.number");
        Mockito.when(userRepository.existsByPhone(userUpdateDTO.getPhone())).thenReturn(true);
        user.setPhone("old.phone.number");
        assertThrows(BadRequestException.class, () ->
                ReflectionTestUtils.invokeMethod(userService, "validateUserDetails", userUpdateDTO, user));
    }

    @Test
    void validateUserDetails_NoException_IfPhoneNull() {
        userUpdateDTO.setPhone(null);
        assertDoesNotThrow(() ->
                ReflectionTestUtils.invokeMethod(userService, "validateUserDetails", userUpdateDTO, user));
    }

    @Test
    void deleteUser_SuccessfulDeletion() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> userService.deleteUser("1"));
    }

    @Test
    void deleteUser_InvalidId_ThrowsBadRequestException() {
        assertThrows(BadRequestException.class, () -> userService.deleteUser("invalid"));
    }


    @Test
    void deleteUser_UserDoesNotExist_ThrowsUserNotFoundException() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("1"));
    }

    @Test
    void deleteUser_ValidId_DeletesUser() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> userService.deleteUser("1"));
        Mockito.verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_ExistingUser_DeleteFails() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(1L);
        assertThrows(RuntimeException.class, () -> userService.deleteUser("1"));
    }

    @Test
    void searchByBirthDateRange_FromDateAfterToDate_ThrowsBadRequestException() {
        LocalDate from = LocalDate.now();
        LocalDate to = from.minusDays(1);
        assertThrows(BadRequestException.class, () -> userService.searchByBirthDateRange(from, to));
    }
    @Test
    void searchByBirthDateRange_ValidDates_ReturnsUsers() {
        LocalDate targetBirthDate = LocalDate.now().minusMonths(6); // Ensures it's between from and to
        user.setBirthDate(targetBirthDate);

        LocalDate from = LocalDate.now().minusYears(1);
        LocalDate to = LocalDate.now();
        List<User> users = List.of(user);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> returnedUsers = userService.searchByBirthDateRange(from, to);
        assertEquals(1, returnedUsers.size());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void searchByBirthDateRange_IncludeUsersWithBirthDateAtRangeBoundaries() {
        LocalDate startRange = LocalDate.of(1990, 1, 1);
        LocalDate endRange = LocalDate.of(1995, 1, 1);

        User userAtStart = new User(1L, "John", "Doe", "john.doe@example.com", startRange, "123 Main St", "+380123456789");
        User userAtEnd = new User(2L, "Jane", "Doe", "jane.doe@example.com", endRange, "456 Elm St", "+380987654321");

        UserDTO userAtStartDTO = new UserDTO(1L, "john.doe@example.com", "John", "Doe", startRange, "123 Main St", "+380123456789");
        UserDTO userAtEndDTO = new UserDTO(2L, "jane.doe@example.com", "Jane", "Doe", endRange, "456 Elm St", "+380987654321");

        Mockito.when(userRepository.findAll()).thenReturn(List.of(userAtStart, userAtEnd));
        Mockito.when(userDTOMapper.apply(userAtStart)).thenReturn(userAtStartDTO);
        Mockito.when(userDTOMapper.apply(userAtEnd)).thenReturn(userAtEndDTO);

        List<UserDTO> results = userService.searchByBirthDateRange(startRange, endRange);

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(user -> user.getBirthDate().equals(startRange)));
        assertTrue(results.stream().anyMatch(user -> user.getBirthDate().equals(endRange)));
    }

    @Test
    void searchByBirthDateRange_IncludeUsersWithinRange() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 1, 1);
        LocalDate birthDateWithinRange = LocalDate.of(1995, 6, 15);

        User userWithinRange = new User(1L, "Alice", "Smith", "alice.smith@example.com", birthDateWithinRange, "789 Oak St", "+380112233445");
        UserDTO userWithinRangeDTO = new UserDTO(1L, "alice.smith@example.com", "Alice", "Smith", birthDateWithinRange, "789 Oak St", "+380112233445");

        Mockito.when(userRepository.findAll()).thenReturn(List.of(userWithinRange));
        Mockito.when(userDTOMapper.apply(userWithinRange)).thenReturn(userWithinRangeDTO);

        List<UserDTO> results = userService.searchByBirthDateRange(from, to);

        assertEquals(1, results.size());
        assertEquals(birthDateWithinRange, results.get(0).getBirthDate());
    }
    @Test
    void searchByBirthDateRange_ExcludeUsersOutsideRange() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 1, 1);
        LocalDate birthDateBeforeRange = LocalDate.of(1980, 6, 15);
        LocalDate birthDateAfterRange = LocalDate.of(2010, 6, 15);
        User userBeforeRange = new User(1L, "John", "Doe", "john.doe@example.com", birthDateBeforeRange, "123 Main St", "+380123456789");
        User userAfterRange = new User(2L, "Jane", "Doe", "jane.doe@example.com", birthDateAfterRange, "124 Main St", "+380123456788");

        Mockito.when(userRepository.findAll()).thenReturn(List.of(userBeforeRange, userAfterRange));

        List<UserDTO> results = userService.searchByBirthDateRange(from, to);

        assertTrue(results.isEmpty());
    }

}