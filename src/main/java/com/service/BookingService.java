package com.service;

import com.model.Booking;
import com.model.Employee;
import com.model.Flight;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingService {
    @PersistenceContext  //This activates EntityManager of Hibernate/JPA
    private EntityManager em;
    public Flight getFlightByID(int id) {
        Flight flight = em.find(Flight.class,id);
        if(flight==null){
            throw new  RuntimeException("Flight is not exist");
        }
        return flight;
    }

    @Transactional
    public void addBooking(Booking booking) {
        em.persist(booking);
    }



    @Transactional
    public Booking getBookingsByBookingIDWithFlightDetails(int bookingId) {
        Booking booking = em.find(Booking.class, bookingId);
        if(booking==null){
            throw new  RuntimeException("Booking is not exist");
        }
        return booking;
    }


    @Transactional
    public void updateBooking(Booking existingBooking) {
        em.merge(existingBooking);

    }

    public List<Booking> getTotalAmountByFlightId(int fId) {
        // Step 0. Validate enum value


        // 1. Build the CriteriaQuery using the Builder : get Builder
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 2. Define the Result Type by creating object : select
        CriteriaQuery<Booking> cq = cb.createQuery(Booking.class);

        // 3. Define the from clause : from
        Root<Booking> root = cq.from(Booking.class);

        // 4. Define the where clause : where (predicate of CQ) jobTitle = ?
        Predicate predicate = cb.equal(root.get("flight").get("id"), fId);
        cq.where(predicate);

        // 5. Execute the criteria query : execution
        return em.createQuery(cq).getResultList();
    }
}
