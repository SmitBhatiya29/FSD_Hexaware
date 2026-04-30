package com.springboot.easypay.repository;

import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.model.TimeSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
    @Query("""
SELECT t FROM TimeSheet t
WHERE  t.employee.manager.id = :managerId
""")
    Page<TimeSheet> findByEmployeeManagerIdAndStatus(
            @Param("managerId") Long managerId,
            Pageable pageable
    );

    @Query("""
        SELECT SUM(t.hoursWorked) FROM TimeSheet t 
        WHERE t.employee.id = ?1
        AND t.status = ?2 
        AND t.workDate BETWEEN ?3 AND ?4""")
    BigDecimal getTotalApprovedHoursForMonth(Long employeeId, RequestStatus requestStatus, LocalDate startDate, LocalDate endDate);

    @Query("""
    SELECT t FROM TimeSheet t 
    where t.employee.user.userEmail = ?1 
    AND (?2 IS NULL OR t.status = ?2)
""")
    List<TimeSheet> getByEmployeeAndStatus(String loggedInEmail, RequestStatus status);

    @Query("""
    select count (ts.id) from  TimeSheet ts 
    where ts.employee.manager.id = ?1 and ts.status = "PENDING"
""")
    int getCountPendingTimeSheet(Long id);

    @Query("""
    SELECT t FROM TimeSheet t 
    where t.employee.user.userEmail = ?1 
    AND (?2 IS NULL OR t.status = ?2)
""")
    Page<TimeSheet> getByEmployeeAndStatusPage(String loggedInEmail, RequestStatus status, Pageable pageable);
}
