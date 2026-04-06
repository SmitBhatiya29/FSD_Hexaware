package com.springboot.easypay.service;

import com.springboot.easypay.dto.PayrollRecordResDto;
import com.springboot.easypay.dto.TeamPayrollSummaryDto;
import com.springboot.easypay.enums.PayrollStatus;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.model.*;
import com.springboot.easypay.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PayrollService {
    private final EmployeeRepository employeeRepository;
    private final PayrollRecordRepository payrollRepository;
    private final SalaryStructureRepository salaryStructureRepository;
    private final EmployeeBenefitRepository employeeBenefitRepository;
    private final TimeSheetRepository timeSheetRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;




    private static final int STANDARD_MONTHLY_HOURS = 160;
    @Transactional
    public void generateDraftPayroll(Long employeeId, String payMonth) {
        log.info("Generating draft payroll for employee ID: {}, month: {}", employeeId, payMonth);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Id is Invalid"));

        // Check if mahine ka payroll pehle se bana hua hai
        payrollRepository.findByEmployeeIdAndPayMonth(employeeId, payMonth)
                .ifPresent(p -> {
                    log.warn("Payroll for employee ID {} and month {} already exists", employeeId, payMonth);
                    throw new RuntimeException("Payroll for this month already exists!");
                });

        SalaryStructure salaryStructure = salaryStructureRepository.getSalaryStructureByEmployeeId(employeeId);
        if(salaryStructure == null) {
            log.error("Salary Structure not found for employee ID: {}", employeeId);
            throw new ResourceNotFoundException("Salary Structure not found for employee");
        }

        //earnings cal karo
        BigDecimal basic = salaryStructure.getBasicSalary();
        BigDecimal hra = salaryStructure.getHraAllowance() != null ? salaryStructure.getHraAllowance() : BigDecimal.ZERO;

        // benefits sum
        List<EmployeeBenefit> benefits = employeeBenefitRepository.findByEmployeeId(employeeId);
        BigDecimal totalBenefits = BigDecimal.ZERO;

        for (EmployeeBenefit benefit : benefits) {
            totalBenefits = totalBenefits.add(benefit.getBenefitAmount());
        }

        // Overtime calculation
        BigDecimal overtimePay = calculateOvertimePay(employeeId, payMonth, basic);

        BigDecimal totalEarnings = basic.add(hra).add(totalBenefits).add(overtimePay);

        //deductions cal
        BigDecimal standardDeduction = salaryStructure.getStandardDeduction() != null ? salaryStructure.getStandardDeduction() : BigDecimal.ZERO;

        // Tax Calculation
        BigDecimal taxPercentage = salaryStructure.getTaxPercentage() != null ? salaryStructure.getTaxPercentage() : BigDecimal.ZERO;
        BigDecimal taxDeduction = basic.multiply(taxPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // LWP Calculation
        BigDecimal lwpDeduction = calculateLwpDeduction(employeeId, basic, hra);

        BigDecimal totalDeductions = standardDeduction.add(taxDeduction).add(lwpDeduction);

        // NET PAYABLE
        BigDecimal netPayable = totalEarnings.subtract(totalDeductions);
        if (netPayable.compareTo(BigDecimal.ZERO) < 0) {
            netPayable = BigDecimal.ZERO; // Salary negative nahi ho sakti
        }

        //SAVE RECORD
        PayrollRecord record = new PayrollRecord();
        record.setEmployee(employee);
        record.setPayMonth(payMonth);
        record.setTotalEarnings(totalEarnings);
        record.setTotalDeductions(totalDeductions);
        record.setNetPayable(netPayable);
        record.setStatus(PayrollStatus.DRAFT);

        payrollRepository.save(record);

        log.info("Draft payroll generated successfully for employee ID: {}, Net Payable: {}", employeeId, netPayable);
    }

    private BigDecimal calculateOvertimePay(Long employeeId, String payMonth, BigDecimal basicSalary) {
        // e.g., "Oct-2025" -> convert to Date Range
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy");
        YearMonth ym = YearMonth.parse(payMonth, formatter);
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();

        BigDecimal totalWorkedHours = timeSheetRepository.getTotalApprovedHoursForMonth(employeeId, RequestStatus.APPROVED, startDate, endDate);
        if (totalWorkedHours == null) totalWorkedHours = BigDecimal.ZERO;

        BigDecimal overtimeHours = totalWorkedHours.subtract(BigDecimal.valueOf(STANDARD_MONTHLY_HOURS));

        if (overtimeHours.compareTo(BigDecimal.ZERO) > 0) {
            // Formula: (Basic / 30 days / 8 hours) * Overtime Hours
            BigDecimal hourlyRate = basicSalary.divide(BigDecimal.valueOf(240), 2, RoundingMode.HALF_UP);
            return overtimeHours.multiply(hourlyRate);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateLwpDeduction(Long employeeId, BigDecimal basic, BigDecimal hra) {
        int currentYear = LocalDate.now().getYear();
        List<LeaveBalance> balances = leaveBalanceRepository.findByEmployeeId(employeeId); // Assume filter by current year is handled

        int totalLwpDays = 0;
        for (LeaveBalance balance : balances) {
            if (balance.getUsedLeaves() > balance.getTotalAllocated()) {
                totalLwpDays += (balance.getUsedLeaves() - balance.getTotalAllocated());
            }
        }

        if (totalLwpDays > 0) {
            // Per day salary = (Basic + HRA) / 30
            BigDecimal perDaySalary = basic.add(hra).divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);
            return perDaySalary.multiply(BigDecimal.valueOf(totalLwpDays));
        }
        return BigDecimal.ZERO;
    }


    public List<PayrollRecordResDto> getDraftPayrolls(String payMonth) {
        return payrollRepository.findByPayMonthAndStatus(payMonth, PayrollStatus.DRAFT)
                .stream()
                .map(this::mapToResDto)
                .toList();
    }

    @Transactional
    public void processPayment(Long payrollId) {
        log.info("Processing payment for payroll record ID: {}", payrollId);
        PayrollRecord record = payrollRepository.findById(payrollId)
                .orElseThrow(() ->{
                    log.error("Payroll record ID {} not found", payrollId);
                    return new ResourceNotFoundException("Payroll record not found");
                });

        if (record.getStatus() == PayrollStatus.PROCESSED) {
            log.warn("Payroll record ID {} is already processed", payrollId);
            throw new RuntimeException("Payroll is already processed!");

        }

        record.setStatus(PayrollStatus.PROCESSED);
        record.setProcessedDate(LocalDate.now());
        payrollRepository.save(record);
        log.info("Payment processed successfully for payroll record ID: {}", payrollId);
    }

    public PayrollRecordResDto getPayStub(String loggedInEmail, String payMonth) {
        Employee employee = employeeRepository.findByUser_UserEmail(loggedInEmail);
        PayrollRecord record = payrollRepository.findByEmployeeIdAndPayMonth(employee.getId(), payMonth)
                .orElseThrow(() -> new ResourceNotFoundException("Pay stub not found"));

        if (record.getStatus() != PayrollStatus.PROCESSED) {
            throw new RuntimeException("Salary is not processed yet for this month.");
        }
        return mapToResDto(record);
    }
    public List<TeamPayrollSummaryDto> getTeamPayrollSummary(String loggedInEmail, String payMonth) {
        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        List<PayrollRecord> records = payrollRepository.getTeamPayrollByManagerAndMonth(manager.getId(), payMonth);
        return records.stream().map(r -> new TeamPayrollSummaryDto(
                r.getEmployee().getId(),
                r.getEmployee().getFirstName() + " " + r.getEmployee().getLastName(),
                r.getTotalEarnings(),
                r.getStatus()
        )).collect(Collectors.toList());
    }

    private PayrollRecordResDto mapToResDto(PayrollRecord r) {
        return new PayrollRecordResDto(
                r.getId(),
                r.getEmployee().getId(),
                r.getEmployee().getFirstName() + " " + r.getEmployee().getLastName(),
                r.getPayMonth(),
                r.getTotalEarnings(),
                r.getTotalDeductions(),
                r.getNetPayable(),
                r.getStatus(),
                r.getProcessedDate()
        );
    }


}
