package com.norbert.clearsolutions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.norbert.clearsolutions.dto.UserDTO;
import com.norbert.clearsolutions.dto.UserUpdateDTO;
import com.norbert.clearsolutions.exception.BadRequestException;
import com.norbert.clearsolutions.exception.GlobalExceptionHandler;
import com.norbert.clearsolutions.service.api.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class)
})
class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private UserDTO userDTO;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(null, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "+380123456789");
        userUpdateDTO = new UserUpdateDTO(1L, "john.update@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "456 New St", "+380987654321");
    }

    @Test
    void update_ValidRequest_ReturnsOk() throws Exception {
        Mockito.doNothing().when(userService).updateUser(Mockito.any(UserUpdateDTO.class));

        mockMvc.perform(put("/api/v1/clearsolutions/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_ValidUserId_ReturnsOk() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/api/v1/clearsolutions/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_InvalidUserId_ReturnsBadRequest() throws Exception {
        Mockito.doThrow(new BadRequestException("Invalid user ID")).when(userService).deleteUser("invalid");

        mockMvc.perform(delete("/api/v1/clearsolutions/user/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchByBirthDateRange_ValidDates_ReturnsUsers() throws Exception {
        Mockito.when(userService.searchByBirthDateRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(Collections.singletonList(userDTO));

        mockMvc.perform(get("/api/v1/clearsolutions/user/searchByBirthDateRange")
                        .param("from", "1990-01-01")
                        .param("to", "2000-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    public void searchByBirthDateRange_FromAfterTo_ReturnsBadRequest() throws Exception {
        String fromDate = "2000-01-01";
        String toDate = "1990-01-01";


        LocalDate fromLocalDate = LocalDate.parse(fromDate);
        LocalDate toLocalDate = LocalDate.parse(toDate);
        Mockito.doThrow(new BadRequestException("Invalid birth date range"))
                .when(userService).searchByBirthDateRange(fromLocalDate, toLocalDate);

        mockMvc.perform(get("/api/v1/clearsolutions/user/searchByBirthDateRange")
                        .param("from", fromDate)
                        .param("to", toDate))
                .andExpect(status().isBadRequest());
    }


    @Test
    void create_ValidRequest_ReturnsCreatedId() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(1L);

        mockMvc.perform(post("/api/v1/clearsolutions/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void create_InvalidRequest_ReturnsBadRequest() throws Exception {
        userDTO.setEmail("invalid-email");

        mockMvc.perform(post("/api/v1/clearsolutions/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }
}
