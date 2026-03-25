package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airline;
import com.model.Flight;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Queue;

@Service
public class FlightService {

    @PersistenceContext  //This activates EntityManager of Hibernate/JPA
    private EntityManager em;

    @Transactional
    public void insert(Airline airline) {
        em.persist(airline);
    }


    public List<?> getAllAirlines() {
        Query queue = em.createQuery("select a from Airline a", Airline.class);
        return queue.getResultList();
    }

    public Airline getAirlineById(int id) {
        Airline airline = em.find(Airline.class, id);
        if (airline == null) {
            throw  new ResourceNotFoundException("Airline with id " + id + " not found");
        }
        return airline;
    }
    @Transactional
    public void insertFlight(Flight flight, String departureTime, Airline airline) {
        LocalTime timeOFDeparture = LocalTime.parse(departureTime, DateTimeFormatter.ISO_LOCAL_TIME);
        flight.setDepartureTime(timeOFDeparture);
        flight.setAirline(airline);
        em.persist(flight);

    }

    public List<Flight> fetchAllFlightWithAirline() {
        String query = "select a from Flight a";
        Query queue = em.createQuery(query);
        return queue.getResultList();
    }

    public Flight getFlightById(int flightId) {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) {
            throw  new ResourceNotFoundException("Flight with id " + flightId + " not found");
        }
        return flight;
    }
}
