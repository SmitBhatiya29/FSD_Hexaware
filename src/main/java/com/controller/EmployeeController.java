package com.controller;

import com.Dto.FlightDto;
import com.config.ProjConfig;
import com.enums.JobTitle;
import com.exception.ResourceNotFoundException;
import com.model.Employee;
import com.service.EmployeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EmployeeController {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(ProjConfig.class);
        EmployeeService employeeService = context.getBean(EmployeeService.class);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. insert Employee");
            System.out.println("2. fetch Employee And Airline Info By Flight");
            System.out.println("3. fetch Employee(s) by job title..");
            System.out.println("4. delete Employee by id");
            System.out.println("5. update Employee by id");
            System.out.println("0. Exit");
            int choice = scanner.nextInt();
            if (choice == 0) {
                break;
            }
            switch (choice) {
                case 1:
                    Employee employee = new Employee();
                    System.out.println("Enter Employee Name: ");
                    employee.setName(scanner.next());
                    System.out.println("Enter Employee Email: ");
                    employee.setEmail(scanner.next());
                    System.out.println("Enter Employee Job Title: ");
                    String jobTitle = scanner.next();
                    System.out.println("Enter Airline ID: ");
                    int airlineId = scanner.nextInt();
                    try {
                        employeeService.insertEmployee(employee,jobTitle,airlineId);
                        System.out.println("Employee inserted successfully");
                    }catch (IllegalArgumentException | ResourceNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                        System.out.println("Enter Flight ID: ");
                    int flightId = scanner.nextInt();
                    FlightDto flightDto = employeeService.fetchFlightWithEmployeeAndAirline(flightId);
                    System.out.println("Flight Number :" + flightDto.flightNumber() + "    " + "source:" + flightDto.source());
                    System.out.println("Airline Name :" + flightDto.airlineName() + "       " +  "destination:" +flightDto.destination());
                    flightDto.employees().forEach(System.out::println);

                    break;
                case 3:
                    System.out.println("Enter Job title: ");
                    Arrays.stream(JobTitle.values()).forEach(System.out::println);
                    String jobTitle1 = scanner.next();
                    try {
                        List<Employee> employeeList = employeeService.getEmployeeByJobTitle(jobTitle1);
                        employeeList.forEach(System.out::println);
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Enter Employee ID: ");
                    int employeeId = scanner.nextInt();
                    employeeService.deleteEmployeeByID(employeeId);
                    System.out.println("Employee Deleted Successfully");
                case 5:
                    int id = 3;
                    Employee updateEmployee = new Employee();
                    updateEmployee.setName("Harsh Bhatiya");

                    Employee updateEmployee2 = new Employee();
                    updateEmployee2.setName("Smit Bhatiya");
                    updateEmployee2.setEmail("smitbhatiya@gmail.com");

                    Employee updateEmployee3 = new Employee();

                    updateEmployee3.setJobTitle(JobTitle.ASST_CAPTAIN);

                    employeeService.updateEmployee(updateEmployee2,id);
                    System.out.println("update successfully..");

                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }

    }
}
