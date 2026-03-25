package com.controller;

import com.config.ProjConfig;

import com.exception.ResourceNotFoundException;
import com.model.Airline;
import com.model.Flight;
import com.service.FlightService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.io.ObjectInputFilter;
import java.util.List;
import java.util.Scanner;

public class FlightController {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjConfig.class);
//        LocalContainerEntityManagerFactoryBean emf =
//                context.getBean(LocalContainerEntityManagerFactoryBean.class);
        FlightService flightService = context.getBean(FlightService.class);

        Airline airline = new Airline();


        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("1. insert Airline");
            System.out.println("2. Show All Airlinea");
            System.out.println("3. Insert Flight");
            System.out.println("4. Show All Flights");
            System.out.println("5. update Flight");
            System.out.println("6. Delete Flight by ID ");
            System.out.println("0. Exit");
            int choice = sc.nextInt();
            if(choice == 0){
                break;
            }
            switch (choice) {
                case 1:

                    System.out.println("Enter Name: ");
                    airline.setName(sc.next());
                    System.out.println("Enter Country: ");
                    airline.setCountry(sc.next());
                    flightService.insert(airline);
                    break;
                case 2:

                    flightService.getAllAirlines().forEach(System.out::println);
                    break;
                case 3:
                    System.out.println("Enter the airline id this flight belongs to : ");
                    int id = sc.nextInt();

                    try{
                        airline = flightService.getAirlineById(id);
                        Flight flight = new Flight();

                        System.out.println("Enter the flight Number : ");
                        flight.setFlightNumber(sc.next());

                        System.out.println("Enter the flight source : ");
                        flight.setSource(sc.next());

                        System.out.println("Enter the flight destination : ");
                        flight.setDestination(sc.next());

                        System.out.println("Enter the flight departure time (hh:mm;:ss): ");
                        String departureTime = sc.next();

                        flightService.insertFlight(flight,departureTime,airline);
                        System.out.println("Flight inserted successfully");

                    }catch (ResourceNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    List<Flight> list = flightService.fetchAllFlightWithAirline();
                    list.forEach(System.out::println);
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;
            }//switch end here

        }//while ends
        sc.close();
        context.close();

    }//main ends
}//class ends
