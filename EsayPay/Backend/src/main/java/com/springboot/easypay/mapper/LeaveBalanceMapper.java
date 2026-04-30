package com.springboot.easypay.mapper;

import com.springboot.easypay.dto.LeaveBalanceStatResDto;
import com.springboot.easypay.model.LeaveBalance;

public class LeaveBalanceMapper {
    public static LeaveBalanceStatResDto mapToDto(LeaveBalance leaveBalance) {
        return new LeaveBalanceStatResDto(
                leaveBalance.getLeaveType(),
                leaveBalance.getTotalAllocated(),
                leaveBalance.getUsedLeaves()
        );
    }
}
