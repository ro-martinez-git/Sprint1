package com.example.DesafioSprint1.controller;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.Request.FlightReservationRequestDTO;
import com.example.DesafioSprint1.dto.Response.FlightReservationResponseDTO;
import com.example.DesafioSprint1.exceptions.HotelFlightBadRequestException;
import com.example.DesafioSprint1.service.IFlightReservationService;
import com.example.DesafioSprint1.service.IFlightService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")

@Validated
public class FlightController {

    private final IFlightService flightService;
    private final IFlightReservationService flightReservationService;

    public FlightController(IFlightService flightService, IFlightReservationService flightReservationService) {
        this.flightService = flightService;
        this.flightReservationService = flightReservationService;
    }


    @GetMapping("/flights")

    public ResponseEntity<?> listFlights(
            @DateTimeFormat(pattern = "dd-MM-yyyy") @RequestParam(value = "date_from", required = false) LocalDate dateFrom,
            @DateTimeFormat(pattern = "dd-MM-yyyy") @RequestParam(value = "date_to", required = false) LocalDate dateTo,
            @RequestParam(value = "origin", required = false) String origin,
            @RequestParam(value = "destination", required = false) String destination) {

        if (origin==null && destination == null && dateFrom == null && dateTo == null )
        {
            return new ResponseEntity<>(flightService.listFlights(), HttpStatus.OK);

        }
        if(origin == null || destination == null || dateFrom == null || dateTo == null)
        {
            // la excepcion que hay que hacer por badreques
            throw new HotelFlightBadRequestException();
        }

        else {
            List<FlightDTO> flightDtoList = flightService.findFlightsByDateAndRoute(dateFrom, dateTo, origin, destination);
            return new ResponseEntity<>(flightDtoList, HttpStatus.OK);
        }


    }

    @PostMapping("/new")
    public ResponseEntity<?> crear(@RequestBody FlightDTO fligth){
        return new ResponseEntity<>(flightService.crear(fligth), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?>borrar(@RequestParam("Id") Long Id) {
        return new ResponseEntity<>(flightService.borrar(Id),HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> actualizarVuelo(@RequestParam("Id") Long Id,
                                             @RequestBody FlightDTO flightDTO) {
        return new ResponseEntity<> (flightService.actualizar(Id, flightDTO), HttpStatus.OK);

    }

    @PostMapping("/flight-reservation/new")
    public ResponseEntity<?> reserveFlight(@RequestBody @Valid FlightReservationRequestDTO request) {
        //FlightReservationResponseDTO response = flightReservationService.reserveFlight(request);
        return new ResponseEntity<>(flightReservationService.reserveFlight(request), HttpStatus.CREATED);
    }



}