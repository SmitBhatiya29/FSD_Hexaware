package com.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String passengerName;
    private String seatNumber;
    private Instant dateOfBooking;
    private BigDecimal fare;
    private int age;
    @ManyToOne
    private Flight flight;

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Instant getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(Instant dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", passengerName='" + passengerName + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", dateOfBooking=" + dateOfBooking +
                ", fare=" + fare +
                ", age=" + age +
                ", flight=" + flight +
                '}';
    }
}
