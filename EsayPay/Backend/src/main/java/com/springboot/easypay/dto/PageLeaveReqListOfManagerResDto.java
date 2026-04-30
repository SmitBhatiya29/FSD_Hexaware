package com.springboot.easypay.dto;

import java.util.List;

public record PageLeaveReqListOfManagerResDto(
        List<LeaveReqListOfManagerResDto>  data,
        long totalRecords,
        int totalPages
) {
}
