package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.exceptions.FlightNotFoundException;
import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.repository.IFlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightServiceUnitTest {

    @Mock
    private IFlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    private List<Flight> flightList;

    @BeforeEach
    void setUp() {
        flightList = Arrays.asList(
                new Flight("FL001", "Buenos Aires", "Córdoba", "Economy", 100.0, LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 10)),
                new Flight("FL002", "Buenos Aires", "Mendoza", "Business", 200.0, LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 10))
        );
    }

    @Test
    void testListFlights() {
        when(flightRepository.findAll()).thenReturn(flightList);

        List<FlightDTO> flights = flightService.listFlights();
        assertEquals(2, flights.size());
    }

    @Test
    void testFindFlightsByDateAndRoute() {
        when(flightRepository.findAll()).thenReturn(flightList);

        List<FlightDTO> flights = flightService.findFlightsByDateAndRoute(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 10), "Buenos Aires", "Córdoba");
        assertEquals(1, flights.size());
        assertEquals("FL001", flights.get(0).getFlightNumber());
    }

    @Test
    void testFindFlightsByDateAndRoute_NotFound() {
        when(flightRepository.findAll()).thenReturn(flightList);

        assertThrows(FlightNotFoundException.class, () -> {
            flightService.findFlightsByDateAndRoute(LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 10), "Buenos Aires", "Salta");
        });
    }
}
