package com.controller;

import com.config.ProjConfig;
import com.model.Fund;
import com.model.Manager;
import com.service.ManagerService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.lang.reflect.AnnotatedArrayType;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class MainController {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjConfig.class);
        LocalContainerEntityManagerFactoryBean emf = context.getBean(LocalContainerEntityManagerFactoryBean.class);
        Scanner sc = new Scanner(System.in);

        ManagerService managerService = context.getBean(ManagerService.class);
        while (true){
            System.out.println("1. Add Manager.");
            System.out.println("2. Add Fund.");
            System.out.println("3. Fetch All Funds for specific Manager.");
            System.out.println("0. Exit.");

            int choice = sc.nextInt();

            if(choice == 0){
                break;
            }
            switch (choice) {
                case 1:
                    Manager manager = new Manager();
                    System.out.println("Enter Name: ");
                    manager.setName(sc.next());
                    System.out.println("Enter Email: ");
                    manager.setEmail(sc.next());
                    managerService.addManager(manager);
                    System.out.println("Manager added successfully.");
                    break;
                case 2:
                    Fund fund = new Fund();
                    System.out.println("Enter Manager Id");
                    int id = sc.nextInt();
                    try {
                        Manager manager1 = managerService.checkId(id);
                        System.out.println("Enter Name: ");
                        fund.setName(sc.next());
                        System.out.println("Enter Amount: ");
                        fund.setAumAmount(sc.nextBigDecimal());
                        System.out.println("Enter Expence Ratio: ");
                        fund.setExepenseRatio(sc.nextBigDecimal());
                        fund.setCreatedAt(Instant.now());
                        fund.setManager(manager1);

                        managerService.addFund(fund);
                        System.out.println("Fund added successfully.");
                    }catch (Exception e){
                        System.out.println("Enter valid ID");
                    }
                    break;
                case 3:
                    System.out.println("Enter Manager Id");
                    int id1 = sc.nextInt();
                    try {
                        managerService.checkId(id1);
                        List<Fund> list = managerService.findFundByManagerID(id1);
                        list.forEach(System.out::println);
                    }catch (Exception e){
                        System.out.println("Enter valid ID");
                    }

                    break;
                default:
                    break;
            }
        }
    }
}
