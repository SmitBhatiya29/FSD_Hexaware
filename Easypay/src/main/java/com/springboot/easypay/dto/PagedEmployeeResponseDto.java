package com.springboot.easypay.dto;

import java.util.List;

public record PagedEmployeeResponseDto(
        List<EmployeesOfManagerResDto> data,
        long totalRecords,
        int totalPages
) {
}
