package com.example.DesafioSprint1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

private String hotelCode;
private String hotelName;
private String destination;
private String roomType;
private Double amount;
@JsonFormat(pattern = "dd-MM-yyyy")
private LocalDate dateFrom;
@JsonFormat(pattern = "dd-MM-yyyy")
private LocalDate dateTo;
private String reserved;


}