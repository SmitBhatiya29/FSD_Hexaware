package com.springboot.easypay.service;

import com.springboot.easypay.dto.LeaveReqDto;
import com.springboot.easypay.dto.ManagerLeaveUpdateDto;
import com.springboot.easypay.enums.LeaveType;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.LeaveRequest;
import com.springboot.easypay.repository.EmployeeRepository;
import com.springboot.easypay.repository.LeaveRequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceService leaveBalanceService;

    public void addLeaveRequest(String loggedUserEmail, LeaveReqDto dto) {
        log.info("Initiating leave request for user: {}", loggedUserEmail);
        //0. get employee by princepal
        Employee employee = employeeRepository.findByUser_UserEmail(loggedUserEmail);
        //1.check leave balance avaliable or not
        int noOfDays = (int) ChronoUnit.DAYS.between(dto.fromDate(), dto.toDate()) + 1;
        LeaveType leaveType = dto.leaveType();
        Year leaveYear = Year.of(dto.fromDate().getYear());
        leaveBalanceService.checkLeaveBalance(employee.getId(),noOfDays,leaveType,leaveYear);

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveType(dto.leaveType());
        leaveRequest.setStartDate(dto.fromDate());
        leaveRequest.setEndDate(dto.toDate());
        leaveRequest.setDetails(dto.details());
        leaveRequest.setEmployee(employee);
        leaveRequestRepository.save(leaveRequest);
        log.info("Leave request submitted successfully for employee ID: {}", employee.getId());
    }

    public List<LeaveRequest> getLeaveRequest(long employeeId) {
        return leaveRequestRepository.getLeaveRequest(employeeId);
    }

    public List<LeaveRequest> getLeaveRequestOfManager(long managerId) {
        return leaveRequestRepository.getLeaveRequestOfManager(managerId);
    }

    @Transactional
    public void  updateLeaveReqest(ManagerLeaveUpdateDto dto,Employee manager) {
        log.info("Manager ID {} updating leave request ID {} to status: {}", manager.getId(), dto.leaveReqId(), dto.status());
        //1 check leave request chhe ke nai
        LeaveRequest leaveRequest = leaveRequestRepository.findById(dto.leaveReqId())
                .orElseThrow(() ->{
                    log.error("Leave request ID {} not found", dto.leaveReqId());
                 return new RuntimeException("Leave request not found");
                });
        if(dto.status() == RequestStatus.APPROVED) {
            //2. update kar balance ne
            leaveBalanceService.checkLeaveBalanceAndUpdate(dto,leaveRequest);
        }
        leaveRequest.setStatus(dto.status());
        leaveRequest.setApprovedBy(manager);
        leaveRequestRepository.save(leaveRequest);
        log.info("Leave request ID {} updated successfully", leaveRequest.getId());
    }

    public List<LeaveRequest> getLeaveRequestOfManagerByStatus(long managerId, RequestStatus rs) {
        return leaveRequestRepository.getLeaveRequestOfManagerByStatus(managerId,rs);
    }

    public LeaveRequest getLeaveReqByLeaveReqId(Long leaveRequestId) {
         return leaveRequestRepository.findById(leaveRequestId).orElseThrow(() -> new RuntimeException("Leave request not found"));
    }
}
