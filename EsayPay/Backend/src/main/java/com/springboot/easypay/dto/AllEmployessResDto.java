package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.enums.Role;

import java.time.LocalDate;

public record AllEmployessResDto(
        Long id,
        String firstName,
        String lastName,
        String userEmail,
        String designation,
        String department,
        String joiningDate,
        Role role,
        RequestStatus status
) {
}
