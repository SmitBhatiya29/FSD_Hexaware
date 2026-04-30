package com.springboot.easypay.dto;

public record PendingEmployeeProfileResDto(
        Long employeeId,
        String firstName,
        String lastName,
        String userEmail,
        String role,
        String department,
        String designation
) {
}
