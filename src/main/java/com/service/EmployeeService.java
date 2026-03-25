package com.service;

import com.Dto.FlightDto;
import com.enums.JobTitle;
import com.model.Airline;
import com.model.Employee;
import com.model.Flight;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    private final FlightService flightService;

    public EmployeeService(FlightService flightService) {
        this.flightService = flightService;
    }
    @PersistenceContext
    private EntityManager em;


    @Transactional
    public void insertEmployee(Employee employee, String jobTitle, int airlineId) {
        JobTitle title = JobTitle.valueOf(jobTitle);
        employee.setJobTitle(title);
        Airline airline = flightService.getAirlineById(airlineId);
        employee.setAirline(airline);
        em.persist(employee);
    }
//    {
//        flightNumber: "",
//                source: "",
//            destination: "",
//            airline_name: "",
//            employees:
//   [
//        {ename: "",jobTitle: "", email: ""},
//        {ename: "",jobTitle: "", email: ""},
//        {ename: "",jobTitle: "", email: ""},
//     ...
//   ]
//    }
//
//    JSON: JavaScript Object Notation
//
//
//    Backend:
//    Create a matching record for the response
//    public record FlightDto(
//            String flightNumber,
//            String source,
//            String destination,
//            String airline_name,
//            List<Employee> employees
//    ) { }
//
//3. Start from Controller
//            Service
//
//    Flight :
//    Airline
//
//    flight airline details
//
//    fetch all employees by airline_id
//    List<Employee>
//
//            FlightDto
    public FlightDto fetchFlightWithEmployeeAndAirline(int flightId) {
        Flight flight = flightService.getFlightById(flightId);

        String jpql = "select e from Employee e where e.airline.id = :flightId";
        Query query = em.createQuery(jpql,Employee.class);
        query.setParameter("flightId",flightId);
        List<Employee> employees = query.getResultList();
        FlightDto flightDto = new FlightDto(
                flight.getFlightNumber(),
                flight.getSource(),
                flight.getDestination(),
                flight.getAirline().getName(),
                employees
        );
        return flightDto;
    }

    public List<Employee> getEmployeeByJobTitle(String jobTitle) {
        // Step 0. Validate enum value
            jobTitle.valueOf(jobTitle);

        // 1. Build the CriteriaQuery using the Builder : get Builder
            CriteriaBuilder  cb = em.getCriteriaBuilder();

        // 2. Define the Result Type by creating object : select
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);

        // 3. Define the from clause : from
            Root<Employee> root = cq.from(Employee.class);

        // 4. Define the where clause : where (predicate of CQ) jobTitle = ?
            Predicate predicate = cb.equal(root.get("jobTitle"), jobTitle);
            cq.where(predicate);

        // 5. Execute the criteria query : execution
            return em.createQuery(cq).getResultList();
    }

    @Transactional
    public void deleteEmployeeByID(int employeeId) {
        em.createQuery("delete from Employee e where e.id = :employeeId").setParameter("employeeId",employeeId).executeUpdate();

    }
    @Transactional
    public void updateEmployee(Employee updateEmployee, int id) {
        // 1. Build the CriteriaQuery using the Builder : get Builder
        CriteriaBuilder cb =  em.getCriteriaBuilder();

        //2. Criteria Update
        CriteriaUpdate<Employee> criteriaUpdate = cb.createCriteriaUpdate(Employee.class);

        // 3. Define the from clause : from
        Root<Employee> employee =  criteriaUpdate.from(Employee.class);

        //4. SET Update
        if(updateEmployee.getName() != null && !updateEmployee.getName().isEmpty())
            criteriaUpdate.set(employee.get("name"), updateEmployee.getName());

        if(updateEmployee.getEmail() != null && !updateEmployee.getEmail().isEmpty())
            criteriaUpdate.set(employee.get("email"), updateEmployee.getEmail());

        if(updateEmployee.getJobTitle() != null)
            criteriaUpdate.set(employee.get("jobTitle"), updateEmployee.getJobTitle());

        // 5. Define the where clause : where (predicate of CQ) id = ?
        Predicate predicate =  cb.equal(employee.get("id"), id);
        criteriaUpdate.where(predicate);

        // 6. Execute the criteria query : execution
        em.createQuery(criteriaUpdate).executeUpdate();
    }
}
