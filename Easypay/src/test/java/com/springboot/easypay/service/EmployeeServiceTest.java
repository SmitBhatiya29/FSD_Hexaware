package com.springboot.easypay.service;

import com.springboot.easypay.dto.LeaveReqOfEmpResDto;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.LeaveRequest;
import com.springboot.easypay.model.User;
import com.springboot.easypay.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveRequestService leaveRequestService;

    @Test
    public void getEmployeeByEmployeeIdTest() {
        Assertions.assertNotNull(employeeService);

        Employee employee = new Employee();
        employee.setId(12L);
        employee.setFirstName("Smit");
        employee.setLastName("Bhatiya");
        employee.setDepartment("IT");

        Mockito.when(employeeRepository.findById(12L)).thenReturn(Optional.of(employee));

        Assertions.assertEquals(employee, employeeService.getEmployeeByEmployeeId(12L));

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(12L);
    }

    @Test
    public void getEmployeeByEmployeeIdTestWhenNotFound() {
        Mockito.when(employeeRepository.findById(10L)).thenReturn(Optional.empty());

        Exception e = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByEmployeeId(10L);
        });

        Assertions.assertEquals("Employee id is invalid", e.getMessage());
    }

    @Test
    public void getLeaveRequestTest() {

        User user = new User();
        user.setUserEmail("smit@test.com");

        Employee employee = new Employee();
        employee.setId(12L);
        employee.setUser(user);

        LeaveRequest lr = new LeaveRequest();
        lr.setId(1L);
        lr.setStatus(RequestStatus.PENDING);
        lr.setStartDate(LocalDate.now());
        lr.setEndDate(LocalDate.now().plusDays(2));
        lr.setDetails("Sick Leave");

        List<LeaveRequest> mockList = List.of(lr);

        Mockito.when(employeeRepository.findByUser_UserEmail("smit@test.com")).thenReturn(employee);
        Mockito.when(leaveRequestService.getLeaveRequest(12L)).thenReturn(mockList);


        List<LeaveReqOfEmpResDto> result = employeeService.getLeaveRequest("smit@test.com");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).id());

        Mockito.verify(employeeRepository, Mockito.times(1)).findByUser_UserEmail("smit@test.com");
        Mockito.verify(leaveRequestService, Mockito.times(1)).getLeaveRequest(12L);
    }
}
