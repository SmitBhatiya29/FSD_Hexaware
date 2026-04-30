package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;

import javax.management.relation.Role;
import java.time.LocalDate;

public record EmployeeProfileResDto(
        long Empid,
        String firstName,
        String lastName,
        String department,
        String designation,
        RequestStatus status,
        String ManagerFirstName,
        String UserEmail,
        String role
) {
}
