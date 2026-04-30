package com.springboot.easypay.controller;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/manager")
@CrossOrigin(origins = "http://localhost:5173/")
public class ManagerController {
    private final ManagerService managerService;

    //manager can get all the employees which are under him / her
    @GetMapping("/get-employees")
    public PagedEmployeeResponseDto getEmployees(Principal principal,
                                                 @RequestParam(value = "page" , required = false,defaultValue = "0") int page,
                                                 @RequestParam(value = "size",required = false,defaultValue = "5") int size) {

        String loggedInEmail = principal.getName();
        return managerService.getEmployees(loggedInEmail,size,page);

    }

    //manager can get list of leave request which are under him/her
    @GetMapping("/get-leave-req")
    public PageLeaveReqListOfManagerResDto getLeaveReq(Principal principal,
                                                       @RequestParam(required = false) String status,
                                                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(value = "size", required = false, defaultValue = "5") int size)
    {
        String loggedInEmail = principal.getName();
        return managerService.getLeaveReq(loggedInEmail,status,page, size);
    }

    //manager can update the leaves approve/rej
    @PutMapping("/update-leave-status")
    public ResponseEntity<?> updateLeaveReqest(@RequestBody ManagerLeaveUpdateDto dto,Principal principal){
        String loggedInEmail = principal.getName();
        managerService.updateLeaveReqest(dto,loggedInEmail);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/stats")
    public ManagerStatResDto getStats(Principal principal){
        String loggedInEmail = principal.getName();
        return  managerService.getStats(loggedInEmail);
    }

    @GetMapping("/department-by-overview")
    public  List<DepartmentByOverviewPaystubResDto> getDepartmentByOverviewPaystub(Principal principal){
        String loggedInEmail = principal.getName();
        return managerService.getDepartmentByOverviewPaystub(loggedInEmail);
    }

}
