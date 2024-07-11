package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.dto.RespuestaDTO;

import java.time.LocalDate;
import java.util.List;

public interface IFlightService {

    List<FlightDTO> listFlights();
    List<FlightDTO> findFlightsByDateAndRoute(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);
    RespuestaDTO crear (FlightDTO requestflightDTO);
    RespuestaDTO actualizar(Long Id, FlightDTO flightDTO);
    RespuestaDTO borrar(Long Id);
}
