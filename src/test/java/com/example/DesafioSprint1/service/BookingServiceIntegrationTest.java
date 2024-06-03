package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.model.Booking;
import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.repository.IHotelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookingServiceIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @MockBean
    private IHotelRepository hotelRepository;

    @Test
    void testMakeBooking() {
        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setUserName("John Doe");
        BookingDTO booking = new BookingDTO();
        booking.setDestination("Córdoba");
        booking.setDateFrom(LocalDate.of(2023, 6, 1));
        booking.setDateTo(LocalDate.of(2023, 6, 10));
        booking.setHotelCode("HTL001");
        booking.setPeople_amount(2);
        booking.setRoomType("Doble");
        bookingRequestDTO.setBookingDTO(booking);

        when(hotelRepository.findAll()).thenReturn(Arrays.asList(
                new Hotel("HTL001", "Hotel Córdoba", "Córdoba", "Doble", 100.0, LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 10), "NO")
        ));

        BookingResponseDTO response = bookingService.makeBooking(bookingRequestDTO);
        assertEquals(1000.0, response.getAmount());
        assertEquals("John Doe", response.getUserName());
    }
}
