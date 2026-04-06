package com.springboot.easypay.dto;

public record EmployeePasswordUpdateDto(
        Long empId,
        String oldPassword,
        String newPassword
) {
}
