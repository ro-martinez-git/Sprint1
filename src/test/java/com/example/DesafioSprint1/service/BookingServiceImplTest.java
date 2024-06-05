package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.PeopleDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.dto.Response.StatusDTO;
import com.example.DesafioSprint1.exceptions.BookingRegistrationException;
import com.example.DesafioSprint1.model.Hotel;
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


@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private static final BookingRequestDTO bookinRequestDTO1 = new BookingRequestDTO(
            "juanperez@gmail.com",
            new BookingDTO(
                    "Cataratas Hotel",
                    LocalDate.parse("10-02-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    LocalDate.parse("20-03-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    "CH-0002",
                    2,
                    "DOBLE",
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez", LocalDate.parse("10-11-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.parse("01-05-1985", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "marialopez@gmail.com")
                    ),
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6)
            )
    );

    private static final BookingRequestDTO bookinRequestDTO2 = new BookingRequestDTO(
            "juanperez@gmail.com",
            new BookingDTO(
                    "Cataratas Hotel",
                    LocalDate.parse("10-02-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    LocalDate.parse("20-03-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    "CH-0002",
                    3,
                    "DOBLE",
                    List.of(
                            new PeopleDTO(12345678, "Juan", "Perez", LocalDate.parse("10-11-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.parse("01-05-1985", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "marialopez@gmail.com")
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
                            new PeopleDTO(12345678, "Juan", "Perez", LocalDate.parse("10-11-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "juanperez@gmail.com"),
                            new PeopleDTO(87654321, "Maria", "Lopez", LocalDate.parse("01-05-1985", DateTimeFormatter.ofPattern("dd-MM-yyyy")), "marialopez@gmail.com")
                    ),
                    new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6) // paymentMethodDTO
            ),
            new StatusDTO("El proceso termino satisfactoriamente", 201) // statusDTO
    );

    public static final Hotel hotel1 = new Hotel(
            "CH-0002",
            "Cataratas Hotel",
            "Puerto Iguazú",
            "Doble",
            6300.00, // Insert the appropriate amount value here
            LocalDate.of(2025, 2, 10),
            LocalDate.of(2025, 3, 20),
            "NO"     );

    public static final Hotel hotel2 = new Hotel(
            "HH-0002",
            "Cataratas Hotel",
            "Puerto Iguazú",
            "Doble",
            6300.00,
            LocalDate.of(2025, 2, 10),
            LocalDate.of(2025, 3, 20),
            "NO"     );

    public static final PaymentMethodDTO paymentMethodDTODebitoOK = new PaymentMethodDTO("DEBIT", "1234-1234-1234-1234", 1);
    public static final PaymentMethodDTO paymentMethodDTOCreditoOK = new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 6);
    public static final PaymentMethodDTO paymentMethodDTOCreditoOK2 = new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 16);
    public static final PaymentMethodDTO paymentMethodDTOTestNOK = new PaymentMethodDTO("CREDITX", "1234-1234-1234-1234", 6);

    @Test
    @DisplayName("Hacer reserva de Hotel OK")
    void makeBookingTestOK() {

        Mockito.when(hotelRepository.findAll()).thenReturn(List.of(hotel1));
        BookingResponseDTO obtenido = bookingService.makeBooking(bookinRequestDTO1);

        Assertions.assertEquals(bookingResponseDTO1, obtenido);
    }

    @Test
    @DisplayName("Hacer reserva de Hotel NOK")
    void makeBookingTestNOK() {

        Mockito.when(hotelRepository.findAll()).thenReturn(List.of(hotel2));
        Assertions.assertThrows(BookingRegistrationException.class, () -> bookingService.makeBooking(bookinRequestDTO1));

    }

    @Test
    @DisplayName("Validar RoomType OK")
    void validateRoomTypeTestOK() {

        Boolean esperado = true;
        Boolean obtenido = bookingService.ValidateRoomType(bookinRequestDTO1);

        Assertions.assertEquals(esperado, obtenido );

    }

    @Test
    @DisplayName("Validar RoomType NOK")
    void validateRoomTypeTestNOK() {

        Boolean esperado = false;
        Boolean obtenido = bookingService.ValidateRoomType(bookinRequestDTO2);

        Assertions.assertEquals(esperado, obtenido );

    }


    @Test
    @DisplayName("Validar metodo de pago DEBITO OK")
    void validatePaymentMethodAndDuesDebitTestOK() {
        PaymentMethodDTO paymentMethodINPUT = paymentMethodDTODebitoOK;
        Boolean esperado = true;

        Boolean obtenido = bookingService.ValidatePaymentMethodAndDues(paymentMethodINPUT);

        Assertions.assertEquals(esperado, obtenido);
    }

    @Test
    @DisplayName("Validar metodo de pago CREDITO OK")
    void validatePaymentMethodAndDuesCreditTestOK() {
        PaymentMethodDTO paymentMethodINPUT = paymentMethodDTOCreditoOK;
        Boolean esperado = true;

        Boolean obtenido = bookingService.ValidatePaymentMethodAndDues(paymentMethodINPUT);

        Assertions.assertEquals(esperado, obtenido);
    }

    @Test
    @DisplayName("Validar metodo de pago NOK")
    void validatePaymentMethodAndDuesTestNOK() {
        PaymentMethodDTO paymentMethodINPUT = paymentMethodDTOTestNOK;
        Boolean esperado = false;

        Boolean obtenido = bookingService.ValidatePaymentMethodAndDues(paymentMethodINPUT);

        Assertions.assertEquals(esperado, obtenido);
    }

    @Test
    @DisplayName("Calcular Interes Debit OK")
    void calculateInterestDebitOK() {
        PaymentMethodDTO paymentMethodINPUT = paymentMethodDTODebitoOK;
        Double esperado = 0.0;

        Double obtenido = bookingService.CalculateInterest(paymentMethodINPUT);

        Assertions.assertEquals(esperado, obtenido);
    }

    @Test
    @DisplayName("Calcular Interes Credit OK")
    void calculateInterestCreditOK() {
        PaymentMethodDTO paymentMethodINPUT = paymentMethodDTOCreditoOK;
        Double esperado = 10.0;

        Double obtenido = bookingService.CalculateInterest(paymentMethodINPUT);

        Assertions.assertEquals(esperado, obtenido);
    }

    @Test
    @DisplayName("Calcular Interes Credit mas de 6 cuotas OK")
    void calculateInterestCreditOK2() {
        PaymentMethodDTO paymentMethodINPUT = paymentMethodDTOCreditoOK2;
        Double esperado = 15.0;

        Double obtenido = bookingService.CalculateInterest(paymentMethodINPUT);

        Assertions.assertEquals(esperado, obtenido);
    }


}