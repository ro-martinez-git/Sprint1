package com.example.DesafioSprint1.controller;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.Request.FlightReservationRequestDTO;
import com.example.DesafioSprint1.dto.Response.FlightReservationResponseDTO;
import com.example.DesafioSprint1.service.IFlightReservationService;
import com.example.DesafioSprint1.service.IFlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")

public class FlightController {

    private final IFlightService flightService;
    private final IFlightReservationService flightReservationService;

    public FlightController(IFlightService flightService, IFlightReservationService flightReservationService) {
        this.flightService = flightService;
        this.flightReservationService = flightReservationService;
    }

    @GetMapping("/flights")
    public ResponseEntity<List<FlightDTO>> listFlights(
            @DateTimeFormat(pattern = "dd-MM-yyyy") @RequestParam("date_from") LocalDate dateFrom,
            @DateTimeFormat(pattern = "dd-MM-yyyy") @RequestParam("date_to") LocalDate dateTo,
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination) {

        if (dateFrom == null && dateTo == null && origin == null && destination == null) {
            return ResponseEntity.badRequest().build();
        }

        List<FlightDTO> flightDtoList = flightService.findFlightsByDateAndRoute(dateFrom, dateTo, origin, destination);
        return new ResponseEntity<>(flightDtoList, HttpStatus.OK);
    }
    @PostMapping("/flight-reservation")
    public ResponseEntity<?> reserveFlight(@RequestBody FlightReservationRequestDTO request) {
        FlightReservationResponseDTO response = flightReservationService.reserveFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}