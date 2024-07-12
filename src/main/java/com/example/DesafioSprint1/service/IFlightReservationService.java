package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.FlightReservationDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Request.FlightReservationRequestDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.dto.Response.FlightReservationResponseDTO;
import com.example.DesafioSprint1.dto.RespuestaDTO;

import java.util.List;

public interface IFlightReservationService {

    FlightReservationResponseDTO reserveFlight(FlightReservationRequestDTO request);

    FlightReservationResponseDTO saveFlightReservation(FlightReservationRequestDTO flightReservationRequestDTO, Long id);

    List<FlightReservationDTO> listaReservasFlight();

    RespuestaDTO deleteFlightReservation(Long Id);

}
