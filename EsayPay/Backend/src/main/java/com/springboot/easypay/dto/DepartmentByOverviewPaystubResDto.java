package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record DepartmentByOverviewPaystubResDto(
        String departmentName,
        BigDecimal totalPayStub
) {
}
