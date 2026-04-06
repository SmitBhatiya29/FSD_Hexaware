package com.springboot.easypay.model;

import com.springboot.easypay.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Year;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "leave_balances")
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "total_allocated", nullable = false)
    private Integer totalAllocated;

    @Column(name = "used_leaves", nullable = false)
    @Builder.Default
    private Integer usedLeaves = 0;

    @Column(name = "leave_year", nullable = false)
    private Year leaveYear;


}
