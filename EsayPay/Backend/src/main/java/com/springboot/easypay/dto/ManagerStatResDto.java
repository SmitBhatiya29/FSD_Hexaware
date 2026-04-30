package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record ManagerStatResDto(
        int teamSize,
        int countPendingLeaveReq,
        int countPendingTimeSheet,
        BigDecimal totalTeamPayStub
) {
}
