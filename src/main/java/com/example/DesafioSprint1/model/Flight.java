package com.example.DesafioSprint1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {

    private String flightNumber;
    private String origin;
    private String destination;
    private String seatType;
    private Double amount;
    private LocalDate dateFrom;
    private LocalDate dateTo;

}
