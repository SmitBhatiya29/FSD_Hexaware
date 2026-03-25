package com.controller;

import com.Dto.FlightDto;
import com.config.ProjConfig;
import com.enums.JobTitle;
import com.exception.ResourceNotFoundException;
import com.model.Booking;
import com.model.Employee;
import com.model.Flight;
import com.service.BookingService;
import com.service.EmployeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BookingController {
    public static void main(String[] args) {

    var context = new AnnotationConfigApplicationContext(ProjConfig.class);
    BookingService bookingService = context.getBean(BookingService.class);
    Scanner scanner = new Scanner(System.in);

        while (true) {
        System.out.println("1. insert a Booking");
        System.out.println("2. fetch Booking along with flight details ");
        System.out.println("3. update Booking");
        System.out.println("4. Display total amount collected for flight based on given data");
        System.out.println("0. Exit");
        int choice = scanner.nextInt();
        if (choice == 0) {
            break;
        }
        switch (choice) {
            case 1:
                Booking booking = new Booking();

                System.out.println("Enter flight number:");
                int id = scanner.nextInt();

                try {
                    Flight flight = bookingService.getFlightByID(id);

                    System.out.println("Enter passenger Name:");
                    scanner.nextLine(); // clear buffer
                    booking.setPassengerName(scanner.nextLine());

                    System.out.println("Enter Seat No:");
                    booking.setSeatNumber(scanner.next());

                    System.out.println("Enter fare:");
                    BigDecimal fare = new BigDecimal(scanner.next());
                    booking.setFare(fare);

                    System.out.println("Enter age:");
                    booking.setAge(Integer.parseInt(scanner.next()));

                    booking.setDateOfBooking(Instant.now());
                    booking.setFlight(flight);

                    bookingService.addBooking(booking);

                    System.out.println("Booking added successfully!");

                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                System.out.println("Enter Booking ID:");
                int BookingId = scanner.nextInt();
                try {
                    Booking b = bookingService.getBookingsByBookingIDWithFlightDetails(BookingId);
                        System.out.println("Passenger Name: " + b.getPassengerName());
                        System.out.println("Seat No: " + b.getSeatNumber());
                        System.out.println("Fare: " + b.getFare());
                        Flight f = b.getFlight();
                        System.out.println("Flight Number: " + f.getFlightNumber());
                        System.out.println("Source: " + f.getSource());
                        System.out.println("Destination: " + f.getDestination());

                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 3:
                System.out.println("Enter booking ID to update:");
                int bookingId = scanner.nextInt();
                scanner.nextLine();

                try {
                    Booking existingBooking = bookingService.getBookingsByBookingIDWithFlightDetails(bookingId);

                    System.out.println("Enter new passenger name:");
                    String newPassengerName = scanner.nextLine();
                    existingBooking.setPassengerName(newPassengerName);

                    System.out.println("Enter new seat number:");
                    String newSeatNumber = scanner.nextLine();
                    existingBooking.setSeatNumber(newSeatNumber);

                    System.out.println("Enter new fare:");
                    BigDecimal newFare = new BigDecimal(scanner.next());
                    existingBooking.setFare(newFare);

                    bookingService.updateBooking(existingBooking);

                    System.out.println("Booking updated successfully!");
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 4:
                System.out.println("Enter flight ID:");
                int fId = scanner.nextInt();

                try {
                    List<Booking> list = bookingService.getTotalAmountByFlightId(fId);
                    BigDecimal totalAmount = BigDecimal.ZERO;
                    for (Booking b : list) {
                        totalAmount = totalAmount.add(b.getFare());
                    }
                    System.out.println("Total Amount Collected: " + totalAmount);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
                break;

            default:
                System.out.println("Invalid choice");
                break;
        }
    }

}
}
