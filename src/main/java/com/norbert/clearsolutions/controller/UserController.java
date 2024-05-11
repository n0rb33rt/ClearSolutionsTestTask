package com.norbert.clearsolutions.controller;

import com.norbert.clearsolutions.dto.UserDTO;
import com.norbert.clearsolutions.dto.UserUpdateDTO;
import com.norbert.clearsolutions.service.api.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clearsolutions/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PutMapping()
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/searchByBirthDateRange")
    public ResponseEntity<List<UserDTO>> searchByBirthDateRange(
            @RequestParam(name = "from") LocalDate from,
            @RequestParam(name = "to") LocalDate to) {

        List<UserDTO> users = userService.searchByBirthDateRange(from, to);
        return ResponseEntity.ok(users);
    }
    @PostMapping
    public ResponseEntity<Map<String, Long>> create(@Valid @RequestBody UserDTO userDTO) {
        Long userId = userService.createUser(userDTO);
        Map<String, Long> response = Collections.singletonMap("id", userId);
        return ResponseEntity.ok(response);
    }
}
