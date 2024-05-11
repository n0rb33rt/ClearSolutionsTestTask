package com.norbert.clearsolutions.controller;

import com.norbert.clearsolutions.dto.UserDTO;
import com.norbert.clearsolutions.dto.UserUpdateDTO;
import com.norbert.clearsolutions.service.api.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clearsolutions/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PutMapping
    @Operation(summary = "Update user", description = "Updates the user data for the given ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data provided", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/searchByBirthDateRange")
    @Operation(summary = "Search users by birth date range", description = "Retrieves users within the specified birth date range")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid date range provided", content = @Content)
    public ResponseEntity<List<UserDTO>> searchByBirthDateRange(
            @RequestParam(name = "from") LocalDate from,
            @RequestParam(name = "to") LocalDate to) {

        List<UserDTO> users = userService.searchByBirthDateRange(from, to);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided user data")
    @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid user data provided", content = @Content)
    public ResponseEntity<Map<String, Long>> create(@Valid @RequestBody UserDTO userDTO) {
        Long userId = userService.createUser(userDTO);
        Map<String, Long> response = Collections.singletonMap("id", userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Deletes a user with the specified ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    public ResponseEntity<Void> delete(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
