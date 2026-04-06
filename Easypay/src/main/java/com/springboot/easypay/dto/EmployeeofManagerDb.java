package com.springboot.easypay.dto;

import com.springboot.easypay.enums.Role;

import java.time.LocalDate;

public record EmployeeofManagerDb(
        String firstName,
        String lastName,
        String department,
        String designation,
        String email,
        Role role,
        LocalDate hireDate
) {
}
