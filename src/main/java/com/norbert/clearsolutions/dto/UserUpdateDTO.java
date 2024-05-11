package com.norbert.clearsolutions.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @NotNull
    private Long id;

    @Email(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String address;

    @Pattern(regexp = "^\\+380\\d{9}$", message = "Phone number must be in format +380XXXXXXXXX")
    private String phone;
}
