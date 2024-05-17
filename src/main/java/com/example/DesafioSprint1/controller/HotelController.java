package com.example.DesafioSprint1.controller;

import java.time.format.DateTimeFormatter;
import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.service.IBookingService;
import com.example.DesafioSprint1.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HotelController {

    @Autowired
    private IHotelService hotelService;
    @Autowired
    private IBookingService bookingService;

    @GetMapping("/hotels")
    public ResponseEntity<?> availableHotels(
    @RequestParam (value="date_from", required = false)  @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateFrom,
    @RequestParam (value="date_to", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy")    LocalDate dateTo,
    @RequestParam (value="destination", required = false) String destination)
    {
        if (dateFrom == null || dateTo == null || destination == null)
        {return new ResponseEntity<>(hotelService.listHotels(), HttpStatus.OK);}
        else {
            return new ResponseEntity<>(hotelService.availableHotels(dateFrom, dateTo, destination), HttpStatus.OK);
        }
    }

        @PostMapping("/booking")
    public ResponseEntity<?> makeBooking(@RequestBody BookingRequestDTO bookingRequestDTO){
        return new ResponseEntity<>(bookingService.makeBooking(bookingRequestDTO), HttpStatus.CREATED);
    }


}

