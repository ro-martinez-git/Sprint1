package com.example.DesafioSprint1.integration;

import com.example.DesafioSprint1.dto.*;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Request.HotelRequestDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.dto.Response.StatusDTO;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
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
public class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private ObjectMapper objectMapper
            = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .registerModule(new JavaTimeModule());

    private ObjectWriter objectWriter = objectMapper.writer();

    private static final HotelDTO HotelDTO1 = new HotelDTO("Cataratas Hotel","Puerto Iguazú", "Doble", LocalDate.of(2025, 02, 10), LocalDate.of(2025, 03, 20), "NO");
    private static final HotelDTO HotelDTO2 = new HotelDTO("Cataratas Hotel 2", "Puerto Iguazú", "Triple", LocalDate.of(2025, 02, 10), LocalDate.of(2025, 03, 23), "NO");
    private static final ErrorDTO errorDTO2 = new ErrorDTO("Se requieren todos los datos para filtrar", 400);

    private static final BookingRequestDTO bookinRequestDTO1 = new BookingRequestDTO(
            "juanperez@gmail.com",
            new BookingDTO(
                    "Cataratas Hotel",
                    //LocalDate.parse("10-02-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    //LocalDate.parse("20-03-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    LocalDate.of(2025, 2, 10),
                    LocalDate.of(2025, 3, 20),
                    "CH-0002",
                    2,
                    "DOBLE",
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez",
                                    LocalDate.of(1982, 11, 10),
                                    "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez",
                                    LocalDate.of(1985, 5, 1),
                                    "marialopez@gmail.com")
                    ),
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6)
            )
    );

    private static final BookingResponseDTO bookingResponseDTO1 = new BookingResponseDTO(
            "juanperez@gmail.com", // userName
            478800.0, // amount
            10.0, // interest
            526680.0, // total
            new BookingDTO(
                    "Cataratas Hotel",
                    LocalDate.of(2025, 2, 10), // dateFrom
                    LocalDate.of(2025, 3, 20), // dateTo
                    "CH-0002", // hotelCode
                    2, // people_amount
                    "DOBLE", // roomType
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez",LocalDate.of(1982, 11, 10), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.of(1985, 5, 1), "marialopez@gmail.com")
                    ),
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6) // paymentMethodDTO
            ),
            new StatusDTO("El proceso termino satisfactoriamente", 201) // statusDTO
    );

    private static final HotelRequestDTO hotelRequestDTO = new HotelRequestDTO ("CH-0002", "Cataratas Hotel", "Puerto Iguazú", "Doble", 6300.0,LocalDate.of(2025, 02,10), LocalDate.of(2025, 03, 20), "NO");

    // Test de listar todos los hoteles disponibles.
    @Test
    public void getAllHotelsTestOK() throws Exception {
        //ACT
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isOk();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(HotelDTO1, HotelDTO2)));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotels"))
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);

    }
    // Test de Listar Hoteles por fechas y destino validos.
    @Test
    public void getHotelsByparamTestOK() throws Exception {
        //ACT
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isOk();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(HotelDTO2)));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotels")
                        .param("date_from", "10-02-2025")
                        .param("date_to", "23-03-2025")
                        .param("destination", "Puerto Iguazú")
                )
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);

    }
    //Test para validar todos los parametros requeridos pasados por @RequestParam.
    @Test
    public void getHotelsBadRequesTestOK() throws Exception {
        //ACT
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isBadRequest();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(errorDTO2));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotels")
                        .param("date_from", "10-02-2025")

                )
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);

    }

    // Test para listar todos los hoteles por codigo de hotel.
    @Test
    public void getHotelsByHotelCodeTestOK() throws Exception {
        //ACT
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isOk();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(HotelDTO2));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/searchHotelCode")
                        .param("hotel_code", "CH-0003")
                )
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);
    }

    // Test para la reserva correcta de un hotel.
    @Test
    public void postHotelBookingTestOK() throws Exception {
        //ACT
        String payLoadJSON = objectWriter.writeValueAsString(bookinRequestDTO1);
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isCreated();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(bookingResponseDTO1));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoadJSON)
                )
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);
    }

    // Test para la creacion de un hotel.
    @Test
    public void postHotelCreateTestOK() throws Exception {
        //ACT
        String payLoadJSON = objectWriter.writeValueAsString(hotelRequestDTO);
        ResultMatcher statusEsperado = MockMvcResultMatchers.status().isCreated();
        ResultMatcher bodyEsperado = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(new RespuestaDTO("Hotel creado con éxito")));
        ResultMatcher contentTypeEsperado = MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON);

        //ASSERT
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/createHotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoadJSON)
                )
                .andExpectAll(statusEsperado, bodyEsperado, contentTypeEsperado);
    }



}
