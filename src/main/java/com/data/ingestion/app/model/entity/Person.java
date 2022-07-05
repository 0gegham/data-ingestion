package com.data.ingestion.app.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Firstname may not be blank")
    @Column(nullable = false)
    private String firstname;
    @NotBlank(message = "Lastname may not be blank")
    @Column(nullable = false)
    private String lastname;
    @NotNull(message = "date may not be null")
    private LocalDate date;
}