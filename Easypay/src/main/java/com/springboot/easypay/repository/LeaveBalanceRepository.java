package com.springboot.easypay.repository;

import com.springboot.easypay.enums.LeaveType;
import com.springboot.easypay.model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Year;
import java.util.List;

public interface LeaveBalanceRepository  extends JpaRepository<LeaveBalance, Long> {


    @Query("""
        SELECT lb FROM LeaveBalance lb
        WHERE lb.employee.id = ?1
        AND lb.leaveType = ?2
        AND lb.leaveYear = ?3
""")
    LeaveBalance findByEmployeeIdAndLeaveTypeAndLeaveYear(Long employeeId, LeaveType leaveType, Year leaveYear);

    @Query("""
    SELECT lb FROM LeaveBalance lb
    WHERE lb.employee.id = ?1 and lb.leaveType = ?2
""")
    LeaveBalance getLeaveBalanceByEmpIdAndLeaveType(Long Empid, LeaveType leaveType);

    List<LeaveBalance> findByEmployeeId(Long employeeId);
}
