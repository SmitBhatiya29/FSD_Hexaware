package com.springboot.easypay.repository;

import com.springboot.easypay.model.EmployeeKyc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeKycRepository extends JpaRepository<EmployeeKyc, Integer> {
}
