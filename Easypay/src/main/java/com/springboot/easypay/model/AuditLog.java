package com.springboot.easypay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Kisne action liya

    @Column(name = "action_type", nullable = false, length = 100)
    private String actionType; // e.g., "SALARY_DISBURSED", "LEAVE_APPROVED"

    @Column(name = "details", length = 500)
    private String details;

    @Column(name = "action_timestamp", nullable = false)
    private LocalDateTime actionTimestamp;
}
