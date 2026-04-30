package com.springboot.easypay.repository;

import com.springboot.easypay.dto.DepartmentByOverviewPaystubResDto;
import com.springboot.easypay.enums.PayrollStatus;
import com.springboot.easypay.model.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {

    @Query("SELECT pr FROM PayrollRecord pr WHERE pr.employee.id = :employeeId AND (:payMonth IS NULL OR pr.payMonth = :payMonth)")
    List<PayrollRecord> findByEmployeeIdAndOptionalPayMonth(@Param("employeeId") Long employeeId, @Param("payMonth") String payMonth);

    Optional<PayrollRecord> findByEmployeeIdAndPayMonth(Long employeeId, String payMonth);

    List<PayrollRecord> findByPayMonthAndStatus(String payMonth, PayrollStatus payrollStatus);

    @Query("""
    select pr from PayrollRecord pr
    WHERE pr.payMonth = ?2
    AND pr.employee.manager.id = ?1
""")
    List<PayrollRecord> getTeamPayrollByManagerAndMonth(Long managerId, String payMonth);

    List<PayrollRecord> findByPayMonth(String payMonth);

    @Query("""
SELECT SUM(p.netPayable) FROM PayrollRecord p WHERE p.payMonth = ?1""")
    BigDecimal sumTotalEarningsByMonth(String month);

    @Query("""
    select SUM(p.netPayable) from PayrollRecord p where p.payMonth = ?1
""")
    BigDecimal getTotalTeamPayStub(String formattedDate);


    @Query("""
    select new com.springboot.easypay.dto.DepartmentByOverviewPaystubResDto(p.employee.department,SUM(p.netPayable)) 
    from PayrollRecord p where p.employee.manager.id = ?1 and p.payMonth = ?2
    group by p.employee.department
""")
    List<DepartmentByOverviewPaystubResDto> getDepartmentByOverviewPaystub(Long id, String formattedDate);
}
