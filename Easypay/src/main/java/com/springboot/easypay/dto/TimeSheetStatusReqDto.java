package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;

public record TimeSheetStatusReqDto(
        RequestStatus requestStatus
) {
}
