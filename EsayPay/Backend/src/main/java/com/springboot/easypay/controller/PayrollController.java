package com.springboot.easypay.controller;

import com.springboot.easypay.dto.PPDashboardStatsDto;
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
@CrossOrigin(origins = "http://localhost:5173/")
@AllArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    //single draft ----------1st step
    @PostMapping("/generate/{employeeId}")
    public ResponseEntity<?> generateDraftPayroll(
            @PathVariable Long employeeId,
            @RequestParam String month) {
       payrollService.generateDraftPayroll(employeeId, month);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //generate for all draft------ 1st step
    @PostMapping("/generate-bulk")
    public ResponseEntity<String> generateBulkDraftPayroll(@RequestParam String month) {
        String result = payrollService.generateBulkDraftPayroll(month);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    //after genrating draft  -------2nd step
    @GetMapping("/drafts")
    public ResponseEntity<List<PayrollRecordResDto>> viewDraftPayrolls(@RequestParam String month) {
        return ResponseEntity.ok(payrollService.getDraftPayrolls(month));
    }

    //payroll proceess -----------3nd step
    @PutMapping("/process/{payrollId}")
    public ResponseEntity<String> processPayment(@PathVariable Long payrollId) {
        payrollService.processPayment(payrollId);
        return ResponseEntity.ok("Payroll Processed successfully");
    }

    // PUT request to mark as paid --- step 3
    @PutMapping("/pay/{payrollId}")
    public ResponseEntity<String> markAsPaid(@PathVariable Long payrollId) {
        payrollService.markAsPaid(payrollId);
        return ResponseEntity.ok("Payroll marked as PAID successfully");
    }

    // GET all payrolls for the master table
    @GetMapping("/all")
    public ResponseEntity<List<PayrollRecordResDto>> getAllPayrolls(@RequestParam String month) {
        return ResponseEntity.ok(payrollService.getAllPayrollsByMonth(month));
    }


    //employee
    @GetMapping("/pay-stub")
    public ResponseEntity<List<PayrollRecordResDto>> getPayStubs(
            Principal principal,
            @RequestParam(required = false) String month) {
        String loggedInEmail = principal.getName();
        return ResponseEntity.ok(payrollService.getPayStubs(loggedInEmail, month));
    }

    @GetMapping("/team-summary")
    public ResponseEntity<List<TeamPayrollSummaryDto>> getTeamSummary(
            Principal principal,
            @RequestParam String month) {
        String loggedInEmail = principal.getName();
        return ResponseEntity.ok(payrollService.getTeamPayrollSummary(loggedInEmail, month));
    }



    @GetMapping("/stats")
    public ResponseEntity<PPDashboardStatsDto> getStats(@RequestParam String month) {
        return ResponseEntity.ok(payrollService.getDashboardStats(month));
    }

}
