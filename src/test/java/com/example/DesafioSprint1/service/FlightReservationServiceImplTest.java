package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.FlightReservationDTO;
import com.example.DesafioSprint1.dto.PeopleDTO;
import com.example.DesafioSprint1.dto.Request.FlightReservationRequestDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.example.DesafioSprint1.dto.Response.FlightReservationResponseDTO;
import com.example.DesafioSprint1.dto.Response.StatusDTO;
import com.example.DesafioSprint1.exceptions.BookingRegistrationException;
import com.example.DesafioSprint1.exceptions.EmptyFlightReservationException;
import com.example.DesafioSprint1.exceptions.InvalidDateFromException;
import com.example.DesafioSprint1.exceptions.InvalidPaymentDebitDues;
import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.repository.IFlightRepository;
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
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FlightReservationServiceImplTest {


    @Mock
    private IFlightRepository flightRepository;

    @InjectMocks
    private FlightReservationServiceImpl flightReservationService;

    private static final FlightReservationRequestDTO flightReservationRequestDTO1 = new FlightReservationRequestDTO(
            "juanperez@gmail.com",
            new FlightReservationDTO(
                    LocalDate.of(2025, 02, 10),
                    LocalDate.of(2025, 02, 20),
                    "Puerto Iguazú",
                    "Bogotá",
                    "PIBA-1420",
                    2,
                    "Economy",
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6),
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez", LocalDate.parse("10-11-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.parse("01-05-1985", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "marialopez@gmail.com")
                    )
            )
    );

    private static final FlightReservationRequestDTO flightReservationRequestDTO2 = new FlightReservationRequestDTO(
            "juanperez@gmail.com",
            new FlightReservationDTO(
                    LocalDate.of(2025, 02, 10),
                    LocalDate.of(2025, 02, 20),
                    "Puerto Iguazú",
                    "Bogotá",
                    "PIBA-1420",
                    2,
                    "Economy",
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6),
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez", LocalDate.parse("10-11-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.parse("01-05-1985", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "marialopez@gmail.com")
                    )
            )
    );

    private static final FlightReservationResponseDTO flightReservationResponseDTO1 = new FlightReservationResponseDTO(
            "juanperez@gmail.com",
            6300.0 *2,
            10.0,
            13860.0,
            new FlightReservationDTO(
                    LocalDate.of(2025, 02, 10),
                    LocalDate.of(2025, 02, 20),
                    "Puerto Iguazú",
                    "Bogotá",
                    "PIBA-1420",
                    2,
                    "Economy",
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6),
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez", LocalDate.parse("10-11-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.parse("01-05-1985", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "marialopez@gmail.com")
                    )
            ),
            new StatusDTO("La reserva finalizó satisfactoriamente", 201)
    );

    public static final Flight flight1 = new Flight(
            "PIBA-1420",
            "Puerto Iguazú",
            "Bogotá",
            "Economy",
            6300.0,
            LocalDate.of(2025, 2, 10),
            LocalDate.of(2025, 2, 20)
    );

    @Test
    @DisplayName("Reservar vuelo OK")
    //public FlightReservationResponseDTO reserveFlight(FlightReservationRequestDTO request)
    void reserveFlightTestOK() {
        Mockito.when(flightRepository.findAll()).thenReturn(List.of(flight1));
        FlightReservationResponseDTO response = flightReservationService.reserveFlight(flightReservationRequestDTO1);
        Assertions.assertEquals(flightReservationResponseDTO1, response);
    }

    @Test
    @DisplayName("Reservar vuelo - vuelo no encontrado")
    void reserveFlightTestFlightNotFound() {
        Mockito.when(flightRepository.findAll()).thenReturn(Arrays.asList());
        Assertions.assertThrows(BookingRegistrationException.class, () -> flightReservationService.reserveFlight(flightReservationRequestDTO1));
    }

    @Test
    @DisplayName("Reservar vuelo - fechas inválidas")
    void reserveFlightTestInvalidDates() {
        FlightReservationRequestDTO invalidDatesRequest = new FlightReservationRequestDTO(
                "juanperez@gmail.com",
                new FlightReservationDTO(
                        LocalDate.of(2025, 02, 20),
                        LocalDate.of(2025, 02, 10),
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
        Assertions.assertThrows(InvalidDateFromException.class, () -> flightReservationService.reserveFlight(invalidDatesRequest));
    }

    @Test
    @DisplayName("Reservar vuelo - usuario no proporcionado")
    void reserveFlightTestEmptyUser() {
        FlightReservationRequestDTO emptyUserRequest = new FlightReservationRequestDTO(
                null,
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
        Assertions.assertThrows(EmptyFlightReservationException.class, () -> flightReservationService.reserveFlight(emptyUserRequest));
    }

    @Test
    @DisplayName("Validar método de pago y cuotas - DEBITO OK")
    void validatePaymentMethodAndDuesDebitTestOK() {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO("DEBIT", "1234-1234-1234-1234", 1);
        Boolean isValid = flightReservationService.ValidatePaymentMethodAndDues(paymentMethodDTO);
        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Validar método de pago y cuotas - CREDITO OK")
    void validatePaymentMethodAndDuesCreditTestOK() {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6);
        Boolean isValid = flightReservationService.ValidatePaymentMethodAndDues(paymentMethodDTO);
        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Validar método de pago y cuotas - CREDITO Invalid")
    void validatePaymentMethodAndDuesCreditTestInvalid() {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 0);
        Boolean isValid = flightReservationService.ValidatePaymentMethodAndDues(paymentMethodDTO);
                Assertions.assertEquals(false, isValid);
    }
}

//    @Test
//    @DisplayName("Obtener todos los vuelos")
//    void getAllFlightsTestOK() {
//        Mockito.when(flightRepository.findAll()).thenReturn(Arrays.asList(flight1));
//        List<FlightDTO> flights = flightReservationService.getAllFlights();
//        Assertions.assertFalse(flights.isEmpty());
//        Assertions.assertEquals(1, flights.size());
//        Assertions.assertEquals(flight1.getFlightNumber(), flights.get(0).getFlightNumber());
//    }
//
//    @Test
//    @DisplayName("Obtener todos los vuelos - No hay vuelos disponibles")
//    void getAllFlightsTestNoFlights() {
//        Mockito.when(flightRepository.findAll()).thenReturn(Arrays.asList());
//        List<FlightDTO> flights = flightReservationService.getAllFlights();
//        Assertions.assertTrue(flights.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Encontrar vuelos por destino")
//    void findFlightByDestinationTestOK() {
//        Mockito.when(flightRepository.findAll()).thenReturn(Arrays.asList(flight1));
//        List<FlightDTO> flights = flightReservationService.reserveFlight.findFlightByDestination("Buenos Aires", "Puerto Iguazú");
//        Assertions.assertFalse(flights.isEmpty());
//        Assertions.assertEquals(1, flights.size());
//        Assertions.assertEquals(flight1.getFlightNumber(), flights.get(0).getFlightNumber());
//    }
//
//    @Test
//    @DisplayName("Encontrar vuelos por destino - No hay vuelos disponibles")
//    void findFlightByDestinationTestNoFlights() {
//        Mockito.when(flightRepository.findAll()).thenReturn(Arrays.asList());
//        List<FlightDTO> flights = flightReservationService.findFlightByDestination("Buenos Aires", "Puerto Iguazú");
//        Assertions.assertTrue(flights.isEmpty());
//    }
//}
