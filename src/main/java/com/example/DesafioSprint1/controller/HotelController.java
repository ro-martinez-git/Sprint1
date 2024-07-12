package com.example.DesafioSprint1.controller;

import java.time.format.DateTimeFormatter;
import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Request.HotelRequestDTO;
import com.example.DesafioSprint1.exceptions.HotelFlightBadRequestException;
import com.example.DesafioSprint1.service.IBookingService;
import com.example.DesafioSprint1.service.IHotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class HotelController {

    @Autowired
    private IHotelService hotelService;
    @Autowired
    private IBookingService bookingService;

    @GetMapping("/hotels")
    public ResponseEntity<?> availableHotels(
    @RequestParam (value="date_from", required = false)  @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateFrom,
    @RequestParam (value="date_to", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateTo,
    @RequestParam (value="destination", required = false) String destination) {

        if (dateFrom == null && dateTo == null && destination == null)
        {
            return new ResponseEntity<>(hotelService.listHotels(), HttpStatus.OK);
        }
        if (dateFrom == null || dateTo == null || destination == null)
        {
            throw new HotelFlightBadRequestException();
        }
        else
        {
            return new ResponseEntity<>(hotelService.availableHotels(dateFrom, dateTo, destination), HttpStatus.OK);
        }

    }
        @PostMapping("/hotel-bookings/new")
    public ResponseEntity<?> makeBooking(@RequestBody @Valid BookingRequestDTO bookingRequestDTO) throws Exception {
        return new ResponseEntity<>(bookingService.makeBooking(bookingRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/hotel-bookings/edit")
    public ResponseEntity<?> saveBooking(@RequestBody @Valid BookingRequestDTO bookingRequestDTO, @RequestParam("id") Long id) throws Exception {
        return new ResponseEntity<>(bookingService.saveBooking(bookingRequestDTO, id), HttpStatus.OK);
    }

    @GetMapping("/hotel-bookings/")
    public ResponseEntity<?> listaBookings() throws Exception {
        return new ResponseEntity<>(bookingService.listaBookings(), HttpStatus.OK);
    }


    @DeleteMapping("/hotel-bookings/delete")
    public ResponseEntity<?> deleteBooking(@RequestParam ("id") Long bookingId) {
        return new ResponseEntity<>(bookingService.deleteBooking(bookingId), HttpStatus.OK);
    }


    @GetMapping("/searchHotelCode")
    public ResponseEntity<?> searchHotelCode(@RequestParam("hotel_code") String hotelCode){
        return new ResponseEntity<>(hotelService.findByHotelCode(hotelCode), HttpStatus.OK);
    }

    @PostMapping("/hotels/new")
    public ResponseEntity<?> createHotel(@RequestBody HotelRequestDTO hotelRequestDTO) {
        return new ResponseEntity<>(hotelService.save(hotelRequestDTO), HttpStatus.CREATED);
    }



    @DeleteMapping("/hotels/delete")
    public ResponseEntity<?> deleteHotel(@RequestParam ("hotel_code") String hotelCode) {
        return new ResponseEntity<>(hotelService.delete(hotelCode), HttpStatus.OK);
    }

    @PutMapping("/hotels/edit")
    public ResponseEntity<?> updateHotel(@RequestParam("id") Long id,
                                         @RequestBody HotelRequestDTO hotelRequestDTO) {
        return new ResponseEntity<>(hotelService.actualizarHotel(id, hotelRequestDTO), HttpStatus.OK);
    }

}

