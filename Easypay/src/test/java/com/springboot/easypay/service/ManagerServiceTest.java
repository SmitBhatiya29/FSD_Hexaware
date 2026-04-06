package com.springboot.easypay.service;

import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.repository.ManagerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {
    @InjectMocks
    private ManagerService managerService;

    @Mock
    private ManagerRepository managerRepository;

    @Test
    public void getByManagerIdTest() {
        Assertions.assertNotNull(managerService);

        Employee manager = new Employee();
        manager.setId(5L);
        manager.setFirstName("Manager Test");

        Mockito.when(managerRepository.findById(5L)).thenReturn(Optional.of(manager));

        Assertions.assertEquals(manager, managerService.getByManagerId(5L));
        Mockito.verify(managerRepository, Mockito.times(1)).findById(5L);
    }

    @Test
    public void getByManagerIdTestWhenNotFound() {
        Mockito.when(managerRepository.findById(10L)).thenReturn(Optional.empty());

        Exception e = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            managerService.getByManagerId(10L);
        });

        Assertions.assertEquals("Manager Id doesnot exist ", e.getMessage());
    }

    @Test
    public void getByManagerIdTestWhenNull() {
        Employee result = managerService.getByManagerId(null);
        Assertions.assertNull(result);
    }
}
