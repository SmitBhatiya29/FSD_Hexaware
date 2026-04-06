package com.springboot.easypay.dto;

import com.springboot.easypay.enums.Role;

public record EmployeesOfManagerResDto(
        String firstName,
        String lastName,
        String department,
        String designation,
        String email,
        Role role,
        long yearsInService
) {
}
