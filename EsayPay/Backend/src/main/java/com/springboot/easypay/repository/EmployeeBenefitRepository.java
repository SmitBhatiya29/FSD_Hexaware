package com.springboot.easypay.repository;

import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.model.Benefit;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.EmployeeBenefit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeBenefitRepository extends JpaRepository<EmployeeBenefit, Long> {
    List<EmployeeBenefit> findByEmployeeId(Long employeeId);

    @Query("""
    select eb from EmployeeBenefit eb 
    where eb.employee.id = ?1 
""")
    Page<EmployeeBenefit> getMyBenefitReqest(Long id, Pageable pageable);

    boolean existsByEmployeeAndBenefit(Employee employee, Benefit benefit);

    @Query("""
    select count (eb.id) from EmployeeBenefit eb
    where eb.employee.id=?1 and eb.status = ?2
""")
    int getPendingBenefitReq(Long id, RequestStatus status);

    @Query("""
SELECT COUNT(e) FROM EmployeeBenefit e WHERE e.status = ?1""")
    long countByStatus(RequestStatus requestStatus);

    @Query(""" 
SELECT SUM(e.benefitAmount) FROM EmployeeBenefit e WHERE e.status = ?1""")
    BigDecimal sumBenefitAmountByStatus(RequestStatus requestStatus);
}
