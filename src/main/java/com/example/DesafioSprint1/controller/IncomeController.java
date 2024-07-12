package com.example.DesafioSprint1.controller;

import com.example.DesafioSprint1.service.IIncomeService;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/")
@Validated
public class IncomeController {

    @Autowired
    private IIncomeService incomeService;
//    @Autowired
//    private IBookingService bookingService;

    @GetMapping("/income")
    public ResponseEntity<?> searchHotelCode(@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date){
        return new ResponseEntity<>(incomeService.getIncomeByDate(date), HttpStatus.OK);
    }

    @GetMapping("/incomeMonth")
    public ResponseEntity<?> getIncomeByMonth(@RequestParam("month") @Range(min = 1, max = 12, message = "El mes debe estar entre 1 y 12") Integer month,
                                              @RequestParam("year") @Min(value = 1900, message = "El año debe ser mayor o igual a 1900") Integer year){
        return new ResponseEntity<>(incomeService.getIncomeByMonth(month, year), HttpStatus.OK);
    }

}

