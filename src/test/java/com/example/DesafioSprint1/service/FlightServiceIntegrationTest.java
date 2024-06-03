package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.repository.IFlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FlightServiceIntegrationTest {

    @Autowired
    private FlightServiceImpl flightService;

    @MockBean
    private IFlightRepository flightRepository;

    @Test
    void testListFlights() {
        when(flightRepository.findAll()).thenReturn(Arrays.asList(
                new Flight("FL001", "Buenos Aires", "Córdoba", "Economy", 100.0, LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 10)),
                new Flight("FL002", "Buenos Aires", "Mendoza", "Business", 200.0, LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 10))
        ));

        List<FlightDTO> flights = flightService.listFlights();
        assertEquals(2, flights.size());
    }
}
