package com.springboot.easypay.dto;

import java.util.List;

public record PageLeaveReqOfEmpResDto(
        List<LeaveReqOfEmpResDto> data,
        long totalRecords,
        int totalPages
) {
}
