package com.example.DesafioSprint1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number")
    private String flightNumber;
    @Column(name = "origin")
    private String origin;
    @Column(name = "destination")
    private String destination;
    @Column(name = "seat_type")
    private String seatType;
    @Column(name = "amount")
    private Double amount;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_from")
    private LocalDate dateFrom;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_to")
    private LocalDate dateTo;

}
