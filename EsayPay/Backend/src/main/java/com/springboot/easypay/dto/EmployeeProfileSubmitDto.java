package com.springboot.easypay.dto;

import jakarta.validation.constraints.NotBlank;

public record EmployeeProfileSubmitDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String department,
        String designation
) {
}
