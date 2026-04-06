package com.springboot.easypay.service;

import com.springboot.easypay.dto.SalaryStructureReqDto;
import com.springboot.easypay.dto.SalaryStructureResDto;
import com.springboot.easypay.dto.SalaryStructureUpdateReqDto;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.SalaryStructure;
import com.springboot.easypay.repository.SalaryStructureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SalaryStructureService {
    private final SalaryStructureRepository salaryStructureRepository;
    private final EmployeeService employeeService;
    

    public void addSalaryStructure(SalaryStructureReqDto salaryStructureReqDto) {
        log.info("Adding salary structure for employee ID: {}", salaryStructureReqDto.employeeId());
        Employee employee = employeeService.getEmployeeByEmployeeId(salaryStructureReqDto.employeeId());
        if (salaryStructureRepository.getSalaryStructureByEmployeeId(employee.getId()) != null) {
            log.warn("Salary structure already exists for employee ID: {}", employee.getId());
            throw new RuntimeException("Salary structure already exists for this employee.");
        }

        SalaryStructure salaryStructure = new SalaryStructure();

        salaryStructure.setEmployee(employee);
        salaryStructure.setBasicSalary(salaryStructureReqDto.basicSalary());
        salaryStructure.setHraAllowance(salaryStructureReqDto.hraAllowance());
        salaryStructure.setStandardDeduction(salaryStructureReqDto.standardDeduction());
        salaryStructure.setTaxPercentage(salaryStructureReqDto.taxPercentage());

        salaryStructureRepository.save(salaryStructure);
        log.info("Salary structure added successfully for employee ID: {}", employee.getId());
    }



    public SalaryStructureResDto getSalaryStructure(long employeeId) {
        return salaryStructureRepository.getSalaryStructure(employeeId);
    }

    public void updateSalaryStucture(SalaryStructureUpdateReqDto salaryStructureUpdateReqDto, Long employeeId) {
        log.info("Updating salary structure for employee ID: {}", employeeId);
        employeeService.getEmployeeByEmployeeId(employeeId);
        SalaryStructure salaryStructure = salaryStructureRepository.getSalaryStructureByEmployeeId(employeeId);
        if (salaryStructure == null) {
            log.error("Salary structure not found for employee ID: {}", employeeId);
            throw new RuntimeException("Salary structure not found for this employee");
        }
        salaryStructure.setBasicSalary(salaryStructureUpdateReqDto.basicSalary());
        salaryStructure.setHraAllowance(salaryStructureUpdateReqDto.hraAllowance());
        salaryStructure.setStandardDeduction(salaryStructureUpdateReqDto.standardDeduction());
        salaryStructure.setTaxPercentage(salaryStructureUpdateReqDto.taxPercentage());

        salaryStructureRepository.save(salaryStructure);

        log.info("Salary structure updated successfully for employee ID: {}", employeeId);
    }


}
