package com.example.DesafioSprint1.controller;

import java.time.format.DateTimeFormatter;
import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.service.IBookingService;
import com.example.DesafioSprint1.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HotelController {

    //Inyectar la dependencia
    @Autowired
    private IHotelService hotelService;
    @Autowired
    private IBookingService bookingService;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> listHotels() {
        return new ResponseEntity<>(hotelService.listHotels(), HttpStatus.OK);
    }
    @GetMapping("/hotelss")
    public ResponseEntity<?> availableHotels(@RequestParam ("date_from")String dateFrom,@RequestParam("date_to")String dateTo, @RequestParam("destination")String destination) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDateFrom = LocalDate.parse(dateFrom, formatter);
        LocalDate localDateTo = LocalDate.parse(dateTo, formatter);
        return new ResponseEntity<>(hotelService.availableHotels(localDateFrom, localDateTo, destination), HttpStatus.OK);
    }

    @PostMapping("/booking")
    public ResponseEntity<?> agregarAlumno(@RequestBody BookingRequestDTO bookingRequestDTO){
        return new ResponseEntity<>(bookingService.makeBooking(bookingRequestDTO), HttpStatus.CREATED);
    }


}

