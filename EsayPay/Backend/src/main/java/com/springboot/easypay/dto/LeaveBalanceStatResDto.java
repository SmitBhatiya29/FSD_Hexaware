package com.springboot.easypay.dto;

import com.springboot.easypay.enums.LeaveType;

public record LeaveBalanceStatResDto(
        LeaveType leaveType,
        Integer totalAllocated,
        Integer usedLeaves
) {
}
