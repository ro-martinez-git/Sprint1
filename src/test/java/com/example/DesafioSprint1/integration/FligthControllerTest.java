package com.example.DesafioSprint1.integration;

import com.example.DesafioSprint1.dto.ErrorDTO;
import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.FlightReservationDTO;
import com.example.DesafioSprint1.dto.PeopleDTO;
import com.example.DesafioSprint1.dto.Request.FlightReservationRequestDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.example.DesafioSprint1.dto.Response.FlightReservationResponseDTO;
import com.example.DesafioSprint1.dto.Response.StatusDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class FligthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper
            = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .registerModule(new JavaTimeModule());

    private ObjectWriter objectWriter = objectMapper.writer();

    private static final FlightDTO fligthdto1=new FlightDTO(
            "BAPI-1235",
            "Buenos Aires",
            "Puerto Iguazú",
            "Economy",
            6500.00,
            LocalDate.of(2025, 02, 10),
            LocalDate.of(2025, 02, 15)
    );

    private static final FlightDTO fligthdto2=new FlightDTO(
            "PIBA-1420",
            "Puerto Iguazú",
            "Bogotá",
            "Business",
            43200.00,
            LocalDate.of(2025, 02, 10),
            LocalDate.of(2025, 02, 20)
    );

    private static final FlightReservationRequestDTO flightReservationRequestDTO1 = new FlightReservationRequestDTO(
            "juanperez@gmail.com",
            new FlightReservationDTO(
                    LocalDate.of(2025, 02, 10),
                    LocalDate.of(2025, 02, 20),
                    "Puerto Iguazú",
                    "Bogotá",
                    "PIBA-1420",
                    2,
                    "Business",
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6),
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez", LocalDate.parse("10-11-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.parse("01-05-1985", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "marialopez@gmail.com")
                    )
            )
    );

    private static final FlightReservationResponseDTO flightReservationResponseDTO1 = new FlightReservationResponseDTO(
            "juanperez@gmail.com",
            43200.0 *2,
            10.0,
            95040.0,
            new FlightReservationDTO(
                    LocalDate.of(2025, 02, 10),
                    LocalDate.of(2025, 02, 20),
                    "Puerto Iguazú",
                    "Bogotá",
                    "PIBA-1420",
                    2,
                    "Business",
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6),
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez", LocalDate.parse("10-11-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.parse("01-05-1985", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "marialopez@gmail.com")
                    )
            ),
            new StatusDTO("La reserva finalizó satisfactoriamente", 201)
    );

    private static final ErrorDTO errorDTO2 = new ErrorDTO("Se requieren todos los datos para filtrar", 400);

    // test para listar todos los vuelos disponibles.
    @Test
    void listFlights() throws Exception  {
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isOk();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(fligthdto1, fligthdto2)));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/flights"))
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);

    }

    // Test de Listar Vuelos por fechas, origen y destino.
    @Test
    public void getFligthByparamTestOK() throws Exception {
        //ACT
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isOk();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(fligthdto1)));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/flights")
                        .param("date_from", "10-02-2025")
                        .param("date_to", "15-02-2025")
                        .param("origin", "Buenos Aires")
                        .param("destination", "Puerto Iguazú")
                )
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);

    }

    // Test para validar cuando vienen parametros vacios.
    @Test
    public void getFligthNullparamTestOK() throws Exception {
        //ACT
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isBadRequest();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(errorDTO2));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/flights")
                        .param("date_from", "10-02-2025")
                )
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);

    }

    // Test para realizar la reserva de un vuelo.
    @Test
    public void postFligthReservationTestOK() throws Exception {
        //ACT
        String payLoadJSON = objectWriter.writeValueAsString(flightReservationRequestDTO1);
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isCreated();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(flightReservationResponseDTO1));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/flight-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoadJSON)
                )
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);
    }


}