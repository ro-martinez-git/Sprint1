package com.example.DesafioSprint1.config;

import com.example.DesafioSprint1.dto.ErrorDTO;
import com.example.DesafioSprint1.dto.ExceptionDTO;
import com.example.DesafioSprint1.exceptions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ReservationInexistentException.class)
    public ResponseEntity<?> registroInexistente(Exception e){
        ErrorDTO error = new ErrorDTO("No se encontraron hoteles para la fecha solicitada", 404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingRegistrationException.class)
    public ResponseEntity<?> bookingRegistrationException(Exception e){
        ErrorDTO error = new ErrorDTO("Los datos de la reserva no coinciden", 404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyFlightReservationException.class)
    public ResponseEntity<?> emptyFlightReservationException(Exception e){
        ErrorDTO error = new ErrorDTO("Los datos de reserva no pueden estar vacios", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<?> FlightNotFoundException(Exception e){
        ErrorDTO error = new ErrorDTO("No hay vuelos para los datos especificados", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<?> HotelNotFoundException(Exception e){
        ErrorDTO error = new ErrorDTO("No existen hoteles con el codigo administrado", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> NullPointerException(Exception e){
        ErrorDTO error = new ErrorDTO("No se recibieron datos", 500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
   }

    @ExceptionHandler(HotelFlightBadRequestException.class)
    public ResponseEntity<?> HotelFlightBadRequest(Exception e){
        ErrorDTO error = new ErrorDTO("Se requieren todos los datos para filtrar", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateRangeFrom.class)
    public ResponseEntity<?> DateRangeFrom(Exception e){
        ErrorDTO error = new ErrorDTO("La fecha de inicio debe ser anterior a la fecha de fin", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    ////// VALIDACIONES //////

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> validationException(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body(
                new ExceptionDTO ("Se encontraron los siguientes errores en las validaciones: @Valid del DTO",
                        e.getAllErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.toList())
                )
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> validationException(ConstraintViolationException e){
        return ResponseEntity.badRequest().body(
                new ExceptionDTO ("Se encontraron los siguientes errores en las validaciones en el PathVariable y RequestParam ",
                        e.getConstraintViolations().stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.toList())
                )
        );
    }

}