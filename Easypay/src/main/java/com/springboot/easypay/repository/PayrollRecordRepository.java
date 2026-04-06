package com.springboot.easypay.repository;

import com.springboot.easypay.enums.PayrollStatus;
import com.springboot.easypay.model.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {
    Optional<PayrollRecord> findByEmployeeIdAndPayMonth(Long employeeId, String payMonth);

    List<PayrollRecord> findByPayMonthAndStatus(String payMonth, PayrollStatus payrollStatus);

    @Query("""
    select pr from PayrollRecord pr
    WHERE pr.payMonth = ?2
    AND pr.employee.manager.id = ?1
""")
    List<PayrollRecord> getTeamPayrollByManagerAndMonth(Long managerId, String payMonth);
}
