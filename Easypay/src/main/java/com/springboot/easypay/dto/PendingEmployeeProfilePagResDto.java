package com.springboot.easypay.dto;

import java.util.List;

public record PendingEmployeeProfilePagResDto(
        List<PendingEmployeeProfileResDto> data,
        long totalRecords,
        int totalPages
) {
}
