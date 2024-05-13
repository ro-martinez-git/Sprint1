package com.example.DesafioSprint1.controller;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.service.IFlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")

public class FlightController {

    @Autowired
    private IFlightService flightService;

    @GetMapping("/flights")
    public ResponseEntity<?> listFlights() {
        return new ResponseEntity<>(flightService.listFlights(), HttpStatus.OK);
    }


}
