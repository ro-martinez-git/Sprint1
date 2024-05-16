package com.example.DesafioSprint1.config;

import com.example.DesafioSprint1.dto.ErrorDTO;
import com.example.DesafioSprint1.exceptions.BookingRegistrationException;
import com.example.DesafioSprint1.exceptions.ReservationInexistentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ReservationInexistentException.class)
    public ResponseEntity<?> registroInexistente(Exception e){
        ErrorDTO error = new ErrorDTO("No se encontraron hoteles para la fecha solicitada", 404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingRegistrationException.class)
    public ResponseEntity<?> bookingRegistrationException(Exception e){
        ErrorDTO error = new ErrorDTO("No fue posible realizar la reserva", 500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}