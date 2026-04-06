package com.springboot.easypay.repository;

import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.model.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
    @Query("""
SELECT t FROM TimeSheet t
WHERE t.status = :requestStatus
AND t.employee.manager.id = :managerId
""")
    List<TimeSheet> findByEmployeeManagerIdAndStatus(
            @Param("managerId") Long managerId,
            @Param("requestStatus") RequestStatus requestStatus
    );

    @Query("""
        SELECT SUM(t.hoursWorked) FROM TimeSheet t 
        WHERE t.employee.id = ?1
        AND t.status = ?2 
        AND t.workDate BETWEEN ?3 AND ?4""")
    BigDecimal getTotalApprovedHoursForMonth(Long employeeId, RequestStatus requestStatus, LocalDate startDate, LocalDate endDate);
}
