package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;

public record ManagerLeaveUpdateDto(
        Long leaveReqId,
        RequestStatus status,
        int noOdDays
        ) {
}
