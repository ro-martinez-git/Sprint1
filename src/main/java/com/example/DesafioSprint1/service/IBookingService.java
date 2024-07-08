package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.dto.RespuestaDTO;

import java.util.List;

public interface IBookingService {

BookingResponseDTO makeBooking(BookingRequestDTO bookingRequestDTO) throws Exception;

BookingResponseDTO saveBooking(BookingRequestDTO bookingRequestDTO, Long id);

List<BookingDTO> listaBookings();

    RespuestaDTO deleteBooking(Long bookingId);
}
