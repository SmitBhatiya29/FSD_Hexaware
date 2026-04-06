package com.springboot.easypay.service;

import com.springboot.easypay.dto.ManagerLeaveUpdateDto;
import com.springboot.easypay.enums.LeaveType;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.LeaveBalance;
import com.springboot.easypay.model.LeaveRequest;
import com.springboot.easypay.repository.LeaveBalanceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@AllArgsConstructor
@Slf4j

public class LeaveBalanceService {
    private final LeaveBalanceRepository leaveBalanceRepository;
    public void addLeaveBalance(Employee employee) {
        log.info("Adding default leave balances for employee ID: {}", employee.getId());
        for(LeaveType leaveType : LeaveType.values()) {
            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setEmployee(employee);
            leaveBalance.setLeaveType(leaveType);
            leaveBalance.setTotalAllocated(10);
            leaveBalance.setLeaveYear(Year.now());
            leaveBalanceRepository.save(leaveBalance);
        }
        log.info("Default leave balances successfully created for employee ID: {}", employee.getId());
    }


    public void checkLeaveBalance(Long empId, int noOfDays, LeaveType leaveType, Year leaveYear) {
        log.info("Checking leave balance for employee ID: {}, Type: {}, Days requested: {}", empId, leaveType, noOfDays);
        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeAndLeaveYear(empId,leaveType,leaveYear);
        if (leaveBalance == null) {
            log.error("Leave Balance record not found for employee ID: {}, Type: {}, Year: {}", empId, leaveType, leaveYear);
            throw new RuntimeException("Leave Balance record not found for this Employee........!");
        }
        int avaBalance = leaveBalance.getTotalAllocated() - leaveBalance.getUsedLeaves();

        if(avaBalance < noOfDays) {
            log.warn("Insufficient leave balance for employee ID: {}. Available: {}, Requested: {}", empId, avaBalance, noOfDays);
            throw new RuntimeException("Insufficent leave Balance , You Have Only " + avaBalance + " leave type " + leaveType);
        }
        log.info("Leave balance is sufficient for employee ID: {}", empId);
    }

    public void checkLeaveBalanceAndUpdate(ManagerLeaveUpdateDto dto,LeaveRequest lr) {
        log.info("Updating leave balance for employee ID: {}, Leave Request ID: {}", lr.getEmployee().getId(), lr.getId());
        LeaveBalance leaveBalance = leaveBalanceRepository.getLeaveBalanceByEmpIdAndLeaveType(lr.getEmployee().getId(),lr.getLeaveType());
        if(dto.noOdDays() > leaveBalance.getTotalAllocated() - leaveBalance.getUsedLeaves()) {
            log.error("Insufficient leaves during update for employee ID: {}", lr.getEmployee().getId());
            throw new RuntimeException("Jis Leave Type ke liye Reqest kiya hai Us type ki leave aapke pass sufficent nhi h ");
        }else{
            int newUsedDays = leaveBalance.getUsedLeaves() + dto.noOdDays();
            leaveBalance.setUsedLeaves(newUsedDays);
            leaveBalanceRepository.save(leaveBalance);
            log.info("Leave balance updated successfully for employee ID: {}", lr.getEmployee().getId());
        }
    }
}
