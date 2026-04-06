package com.springboot.easypay.service;

import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.enums.Role;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.User;
import com.springboot.easypay.repository.HrRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HrServiceTest{
    @InjectMocks
    private HrService hrService;

    @Mock
    private HrRepository hrRepository;

    @Test
    public void getAllManagerTest() {
        Assertions.assertNotNull(hrService);

        Employee manager1 = new Employee();
        manager1.setId(12L);
        manager1.setFirstName("John");
        manager1.setLastName("Doe");
        manager1.setDepartment("HR");

        List<Employee> list = List.of(manager1);

       Mockito.when(hrRepository.getAllManager()).thenReturn(list);

        Assertions.assertEquals(1, hrService.getAllManager().size());
        Assertions.assertEquals("John", hrService.getAllManager().get(0).firstName());

        Mockito.verify(hrRepository, Mockito.times(2)).getAllManager(); // Called twice because of the two assertions
    }

    @Test
    public void getAllPendingProfilesTest() {

        User user = new User();
        user.setUserEmail("test@gmail.com");


        Employee emp1 = new Employee();
        emp1.setUser(user);
        emp1.setId(14L);
        emp1.setFirstName("Test");
        emp1.setStatus(RequestStatus.PENDING);

        List<Employee> list = List.of(emp1);

        // Create a pageObj for page 0 and size 1
        Page<Employee> pageEmployee = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 1);

          Mockito.when(hrRepository.findByStatus(RequestStatus.PENDING, Role.EMPLOYEE, "IT", pageable)).thenReturn(pageEmployee);

          Assertions.assertEquals(1, hrService.getAllPendingProfiles(0, 1, Role.EMPLOYEE, "IT").data().size());
        Assertions.assertEquals(1, hrService.getAllPendingProfiles(0, 1, Role.EMPLOYEE, "IT").totalRecords());
    }
}
