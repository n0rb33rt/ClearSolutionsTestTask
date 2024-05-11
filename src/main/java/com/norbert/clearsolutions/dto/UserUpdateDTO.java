package com.norbert.clearsolutions.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @Schema(description = "Unique identifier of the user being updated", example = "1", required = true)
    @NotNull
    private Long id;

    @Schema(description = "Updated email address of the user", example = "updated.email@example.com", required = false)
    @Email(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    @Schema(description = "Updated first name of the user", example = "Jane", required = false)
    private String firstName;

    @Schema(description = "Updated last name of the user", example = "Doe", required = false)
    private String lastName;

    @Schema(description = "Updated birth date of the user", example = "1988-05-15", required = false)
    private LocalDate birthDate;

    @Schema(description = "Updated home address of the user", example = "5678 Elm St", required = false)
    private String address;

    @Schema(description = "Updated phone number of the user", example = "+380987654321", required = false)
    @Pattern(regexp = "^\\+380\\d{9}$", message = "Phone number must be in format +380XXXXXXXXX")
    private String phone;
}
