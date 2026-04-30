package com.springboot.easypay.repository;

import com.springboot.easypay.model.PayrollPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface PayrollPolicyRepository extends JpaRepository<PayrollPolicy, Long> {
    @Query("SELECT p FROM PayrollPolicy p WHERE :yearlySalary >= p.minSalary AND :yearlySalary < p.maxSalary")
    Optional<PayrollPolicy> findPolicyBySalaryRange(@Param("yearlySalary") BigDecimal yearlySalary);
}
