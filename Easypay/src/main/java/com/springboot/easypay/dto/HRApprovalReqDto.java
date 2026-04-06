package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.enums.Role;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HRApprovalReqDto(
        Long managerId,
        @NotNull
        Role role,
        @NotNull(message = "Joining date is required")
        LocalDate joiningDate,
        RequestStatus status
) {
}
