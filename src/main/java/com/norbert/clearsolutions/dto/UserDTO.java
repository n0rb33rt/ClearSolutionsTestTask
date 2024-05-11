package com.norbert.clearsolutions.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserDTO {
    @Schema(description = "Unique identifier of the user. Not required when creating a new user.", example = "1", required = false)
    private Long id;

    @Schema(description = "Email address of the user", example = "user@example.com", required = true)
    @Email(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @NotBlank(message = "Email can not be blank")
    private String email;

    @Schema(description = "First name of the user", example = "John", required = true)
    @NotBlank(message = "First name can not be blank")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe", required = true)
    @NotBlank(message = "Last name can not be blank")
    private String lastName;

    @Schema(description = "Birth date of the user", example = "1990-01-01", required = true)
    @NotNull(message = "Birth date can not be null")
    private LocalDate birthDate;

    @Schema(description = "Home address of the user", example = "1234 Main St", required = false)
    private String address;

    @Schema(description = "Phone number of the user", example = "+380123456789", required = false)
    @Pattern(regexp = "^\\+380\\d{9}$", message = "Phone number must be in format +380XXXXXXXXX")
    private String phone;
}
