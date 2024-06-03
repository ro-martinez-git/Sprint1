package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.exceptions.BookingRegistrationException;
import com.example.DesafioSprint1.model.Booking;
import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.repository.IHotelRepository;
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
public class BookingServiceUnitTest {

    @Mock
    private IHotelRepository hotelRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequestDTO bookingRequestDTO;
    private List<Hotel> hotelList;

    @BeforeEach
    void setUp() {
        bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setUserName("John Doe");
        BookingDTO booking = new BookingDTO();
        booking.setDestination("Córdoba");
        booking.setDateFrom(LocalDate.of(2023, 6, 1));
        booking.setDateTo(LocalDate.of(2023, 6, 10));
        booking.setHotelCode("HTL001");
        booking.setPeople_amount(2);
        booking.setRoomType("Doble");
        bookingRequestDTO.setBookingDTO(booking);

        hotelList = Arrays.asList(
                new Hotel("HTL001", "Hotel Córdoba", "Córdoba", "Doble", 100.0, LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 10), "NO")
        );
    }

    @Test
    void testMakeBooking() {
        when(hotelRepository.findAll()).thenReturn(hotelList);

        BookingResponseDTO response = bookingService.makeBooking(bookingRequestDTO);
        assertEquals(1000.0, response.getAmount());
        assertEquals("John Doe", response.getUserName());
    }

    @Test
    void testMakeBooking_HotelNotFound() {
        when(hotelRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(BookingRegistrationException.class, () -> {
            bookingService.makeBooking(bookingRequestDTO);
        });
    }
}
