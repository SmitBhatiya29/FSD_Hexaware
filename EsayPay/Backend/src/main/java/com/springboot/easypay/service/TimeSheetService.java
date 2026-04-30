package com.springboot.easypay.service;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.mapper.TimeSheetMapper;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.TimeSheet;
import com.springboot.easypay.repository.EmployeeRepository;
import com.springboot.easypay.repository.TimeSheetRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TimeSheetService {
    private TimeSheetRepository timeSheetRepository;
    private final EmployeeRepository employeeRepository;
    public void submitTimeSheet(TimeSheetReqDto dto, String loggedInEmail) {
        log.info("Submitting timesheet for user email: {} for date: {}", loggedInEmail, dto.workDate());
        Employee employee =employeeRepository.findByUser_UserEmail(loggedInEmail);
        System.out.println(dto);
        TimeSheet timeSheet = new TimeSheet();
        timeSheet.setEmployee(employee);
        timeSheet.setWorkDate(dto.workDate());
        timeSheet.setHoursWorked(dto.hoursWorked());
        timeSheet.setProject(dto.project());
        timeSheet.setDescription(dto.Description());
        timeSheet.setStatus(RequestStatus.PENDING);
        timeSheetRepository.save(timeSheet);
        log.info("Timesheet submitted successfully for employee ID: {}", employee.getId());
    }
    public PageTimeSheetResponseDto getPendingTimeSheetsForManager(String loggedInEmail,int page, int size) {

        log.info("Fetching pending timesheets for manager email: {}", loggedInEmail);
        Pageable pageable =  PageRequest.of(page, size);

        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        long managerId = manager.getId();
        System.out.println("ManagerId: " + managerId);

        Page<TimeSheet> pendingSheets = timeSheetRepository.findByEmployeeManagerIdAndStatus(managerId,pageable);

        System.out.println(pendingSheets);
        log.info("Found {} pending timesheets for manager ID: {}", manager.getId());
        List<TimeSheetResponseDto> data = pendingSheets
                .stream()
                .map(TimeSheetMapper::mapToResponseDto)
                .toList();

        return new PageTimeSheetResponseDto(data,pendingSheets.getTotalElements(),pendingSheets.getTotalPages());
    }

    public void updateTimeSheetStatus(Long timesheetId, String loggedInEmail, TimeSheetStatusReqDto dto) {
        log.info("Manager email {} updating timesheet ID {} to status: {}", loggedInEmail, timesheetId, dto.requestStatus());
        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        long managerId = manager.getId();
        TimeSheet timeSheet = timeSheetRepository.findById(timesheetId)
                .orElseThrow(() ->{
                    log.error("Timesheet ID {} is invalid", timesheetId);
                     return new ResourceNotFoundException("Timesheet id invalid ");
                });

        if(!timeSheet.getEmployee().getManager().getId().equals(managerId)){
            log.error("Manager ID {} is not authorized to update timesheet ID {}", manager.getId(), timesheetId);
            throw new ResourceNotFoundException("Manager id invalid ");
        }

        timeSheet.setStatus(dto.requestStatus());

        timeSheetRepository.save(timeSheet);
        log.info("Timesheet ID {} updated successfully", timesheetId);

    }

    public List<EmpTimeSheetResDto> getTimeSheet(String loggedInEmail, RequestStatus status) {
        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        List<TimeSheet> list = timeSheetRepository.getByEmployeeAndStatus(loggedInEmail,status);
        return list.stream().map(TimeSheetMapper::mapToEmpResDto).toList();
    }

    public PageEmpTimeSheetResDto getTimeSheetPage(String loggedInEmail, RequestStatus status,int page,int size) {
        Pageable pageable =  PageRequest.of(page, size);

        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);

        Page<TimeSheet> list = timeSheetRepository.getByEmployeeAndStatusPage(loggedInEmail,status,pageable);
        List<EmpTimeSheetResDto> data =  list.stream().map(TimeSheetMapper::mapToEmpResDto).toList();
        return new PageEmpTimeSheetResDto(data,list.getTotalElements(),list.getTotalPages());
    }
}
