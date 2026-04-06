package com.springboot.easypay.repository;

import com.springboot.easypay.dto.SalaryStructureResDto;
import com.springboot.easypay.model.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {

    @Query("""
    select ss from SalaryStructure ss
    where ss.employee.id = ?1
""")
    SalaryStructure getSalaryStructureByEmployeeId(Long id);

    @Query("""
    select ss from SalaryStructure ss
    where ss.employee.id = ?1
""")
    SalaryStructureResDto getSalaryStructure(long employeeId);


}
