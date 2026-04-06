package com.springboot.easypay.controller;

import com.springboot.easypay.dto.PayrollRecordResDto;
import com.springboot.easypay.dto.TeamPayrollSummaryDto;
import com.springboot.easypay.service.PayrollService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@AllArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    @PostMapping("/generate/{employeeId}")
    public ResponseEntity<?> generateDraftPayroll(
            @PathVariable Long employeeId,
            @RequestParam String month) {
       payrollService.generateDraftPayroll(employeeId, month);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/drafts")
    public ResponseEntity<List<PayrollRecordResDto>> viewDraftPayrolls(@RequestParam String month) {
        return ResponseEntity.ok(payrollService.getDraftPayrolls(month));
    }

    @PutMapping("/process/{payrollId}")
    public ResponseEntity<String> processPayment(@PathVariable Long payrollId) {
        payrollService.processPayment(payrollId);
        return ResponseEntity.ok("Payroll Processed successfully");
    }

    @GetMapping("/pay-stub")
    public ResponseEntity<PayrollRecordResDto> getPayStub(
            Principal principal,
            @RequestParam String month) {
        String loggedInEmail = principal.getName();
        return ResponseEntity.ok(payrollService.getPayStub(loggedInEmail, month));
    }

    @GetMapping("/team-summary")
    public ResponseEntity<List<TeamPayrollSummaryDto>> getTeamSummary(
            Principal principal,
            @RequestParam String month) {
        String loggedInEmail = principal.getName();
        return ResponseEntity.ok(payrollService.getTeamPayrollSummary(loggedInEmail, month));
    }

}
