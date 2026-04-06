package com.springboot.easypay.repository;

import com.springboot.easypay.model.EmployeeBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeBenefitRepository extends JpaRepository<EmployeeBenefit, Long> {
    List<EmployeeBenefit> findByEmployeeId(Long employeeId);
}
