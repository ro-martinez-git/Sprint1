package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.exceptions.DateRangeFrom;
import com.example.DesafioSprint1.exceptions.FlightNotFoundException;
import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.repository.FlightRepository;
import com.example.DesafioSprint1.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)


class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImpl flightService ;

    private static final FlightDTO fligthdto1=new FlightDTO(
            "BAPI-1235",
            "Buenos Aires",
            "Puerto Iguazú",
            "Economy",
            6500.00,
            LocalDate.parse("10-02-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
            LocalDate.parse("15-02-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    );



    private static final Flight fligth1=new Flight(
            "BAPI-1235",
            "Buenos Aires",
            "Puerto Iguazú",
            "Economy",
            6500.00,
            LocalDate.parse("10-02-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
            LocalDate.parse("15-02-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    );

    @Test
    @DisplayName("listar vuelos ok")

    void listFlightsTestOK() {
        List<FlightDTO>esperado=List.of(fligthdto1);
        Mockito.when(flightRepository.findAll()) .thenReturn(List.of(fligth1));

        List<FlightDTO>obtenido=flightService.listFlights();

        Assertions.assertEquals(esperado,obtenido);
    }

    @Test
    @DisplayName("Buscar vuelos test ok")
    void findFlightsByDateAndRoute() {
        LocalDate dateFromEntrada = fligth1.getDateFrom();
        LocalDate dateToEntrada= fligth1.getDateTo();
        String destinationEntrada= fligth1.getDestination();
        String origenEntrada= fligth1.getOrigin();

        List<Flight>listaVuelos= List.of(fligth1);
        List<FlightDTO>listaEsperada= List.of(fligthdto1);

        Mockito.when(flightRepository.findAll()).thenReturn(listaVuelos);
        List<FlightDTO>listaObtenida=flightService.findFlightsByDateAndRoute(dateFromEntrada, dateToEntrada, origenEntrada,destinationEntrada);

        Assertions.assertEquals(listaEsperada,listaObtenida);


    }
    @Test
    @DisplayName("Vuelos no encontrados")
    void findFlightsByDateAndRoute2() {
        LocalDate dateFromEntrada = fligth1.getDateFrom();
        LocalDate dateToEntrada= fligth1.getDateTo();
        String destinationEntrada= fligth1.getDestination();
        String origenEntrada= fligth1.getOrigin();

        Assertions.assertThrows(FlightNotFoundException.class,
                ()-> flightService.findFlightsByDateAndRoute(dateFromEntrada, dateToEntrada, origenEntrada,destinationEntrada));

    }
    @Test
    @DisplayName("Validacion entre fechas test ok")
    void findFlightsByDateAndRoute3() {
        LocalDate dateFromEntrada =  LocalDate.parse("15-02-2026", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate dateToEntrada= fligth1.getDateTo();
        String destinationEntrada= fligth1.getDestination();
        String origenEntrada= fligth1.getOrigin();

        Assertions.assertThrows(DateRangeFrom.class,
                ()-> flightService.findFlightsByDateAndRoute(dateFromEntrada, dateToEntrada, origenEntrada,destinationEntrada));

    }

}
