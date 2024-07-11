package com.example.DesafioSprint1.config;

import com.example.DesafioSprint1.dto.ErrorDTO;
import com.example.DesafioSprint1.dto.ExceptionDTO;
import com.example.DesafioSprint1.exceptions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
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

    @ExceptionHandler(HotelBookingAlreadyRegisteredException.class)
    public ResponseEntity<?> hotelBookingAlreadyRegisteredException(Exception e){
        ErrorDTO error = new ErrorDTO("Ya existe una reserva con las mismas caracteristicas", 404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FlightExistException.class)
    public ResponseEntity<?> FlighExistException(Exception e){
        ErrorDTO error = new ErrorDTO("Este número de vuelo ya se encuentra registrado", 404);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FlightNoExistException.class)
    public ResponseEntity<?> FlightNoExistException(Exception e){
        ErrorDTO error = new ErrorDTO("No hay vuelos para el ID ingresado", 404);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
    @ExceptionHandler(HotelNotAvailableException.class)
    public ResponseEntity<?> HotelNotAvailableException(Exception e){
        ErrorDTO error = new ErrorDTO("No existen hoteles disponibles con los datos administrados", 400);
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

    @ExceptionHandler(flightRangeException.class)
    public ResponseEntity<?> flightRange(Exception e){
        ErrorDTO error = new ErrorDTO("La fecha del vuelo debe ser posterior a la fecha actual", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidDateFromException.class)
    public ResponseEntity<?> InvalidDateFromException(Exception e){
        ErrorDTO error = new ErrorDTO("La fecha de entrada debe ser menor a la de salida", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRoomTypeException.class)
    public ResponseEntity<?> InvalidRoomTypeException(Exception e){
        ErrorDTO error = new ErrorDTO("El tipo de habitación seleccionada no coincide con la cantidad de personas que se alojarán en ella.", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPaymentDebitDues.class)
    public ResponseEntity<?> InvalidPaymentDebitDues(Exception e){
        ErrorDTO error = new ErrorDTO("Tarjeta de débito: Se ha ingresado una cantidad de cuotas diferente a 1", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableException(Exception e){
        ErrorDTO error = new ErrorDTO("Solo se aceptan valores numéricos", 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoBookingsException.class)
    public ResponseEntity<?> NoBookingsException(Exception e){
        ErrorDTO error = new ErrorDTO("No hay reservas de hotel registradas", 404);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class, DateTimeParseException.class})
    public ResponseEntity<String> handleDateParseException(Exception ex) {
        return new ResponseEntity<>("Fecha invalida. Formato valido : dd-MM-yyyy.", HttpStatus.BAD_REQUEST);
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