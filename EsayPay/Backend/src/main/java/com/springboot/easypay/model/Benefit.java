package com.springboot.easypay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "benefits")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Benefit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "benefit_name", nullable = false, unique = true, length = 100)
    private String benefitName;

    @Column(name = "description", length = 255)
    private String description;
}
