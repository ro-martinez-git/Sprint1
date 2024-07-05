package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.dto.Response.StatusDTO;
import com.example.DesafioSprint1.exceptions.*;
import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.repository.IBookingRepository;
import com.example.DesafioSprint1.repository.IHotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService{

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private IHotelRepository hotelRepository;

    @Autowired
    private IBookingRepository bookingRepository;


    @Override
    public BookingResponseDTO makeBooking(BookingRequestDTO bookingRequestDTO) {
        if(bookingRequestDTO.getBookingDTO().getDateFrom().isAfter(bookingRequestDTO.getBookingDTO().getDateTo())
                || bookingRequestDTO.getBookingDTO().getDateFrom().isEqual(bookingRequestDTO.getBookingDTO().getDateTo()))
        {
            throw new InvalidDateFromException();
        }

        if ( ! ValidateRoomType(bookingRequestDTO) )
        {   throw new InvalidRoomTypeException(); }

        if (! ValidatePaymentMethodAndDues(bookingRequestDTO.getBookingDTO().getPaymentMethodDTO()))
        { if (bookingRequestDTO.getBookingDTO().getPaymentMethodDTO().getType().toUpperCase().equals("DEBIT"))
        {throw new InvalidPaymentDebitDues();
        }

        }

        // El criterio para filtrar el hotel es coincidencia de HotelCode y RoomType.
        BookingResponseDTO answer = new BookingResponseDTO();
        Double interest = this.CalculateInterest(bookingRequestDTO.getBookingDTO().getPaymentMethodDTO());
        Long days = bookingRequestDTO.getBookingDTO().getDateTo().toEpochDay() - bookingRequestDTO.getBookingDTO().getDateFrom().toEpochDay();
        List<Hotel> hotels = hotelRepository.findAll().stream()
                .filter(hotel -> bookingRequestDTO.getBookingDTO().getHotelCode().equals(hotel.getHotelCode()))
                .filter(hotel -> bookingRequestDTO.getBookingDTO().getRoomType().toUpperCase().equals(hotel.getRoomType().toUpperCase()))
                .toList();
        if(hotels.isEmpty() ) { throw new BookingRegistrationException(); }
        Double amount =  hotels.get(0).getAmount() * days * bookingRequestDTO.getBookingDTO().getPeople_amount() ;
        Double total = ( ( amount * interest / 100 ) + amount ) ;
        answer.setUserName(bookingRequestDTO.getUserName());
        answer.setAmount(amount);
        answer.setInterest(interest);
        answer.setTotal(total);
        answer.setBookingDTO( bookingRequestDTO.getBookingDTO() );
        answer.setStatusDTO(new StatusDTO("El proceso termino satisfactoriamente", 201));

        return answer;
    }

    public Boolean ValidateRoomType(BookingRequestDTO bookingRequestDTO)
    {

        if (bookingRequestDTO.getBookingDTO().getRoomType().toUpperCase().equals("SINGLE") &&
                bookingRequestDTO.getBookingDTO().getPeople_amount().equals(1)
        ){return true;}
        if (bookingRequestDTO.getBookingDTO().getRoomType().toUpperCase().equals("DOBLE") &&
                bookingRequestDTO.getBookingDTO().getPeople_amount().equals(2)
        ){return true;}
        if (bookingRequestDTO.getBookingDTO().getRoomType().toUpperCase().equals("TRIPLE") &&
                bookingRequestDTO.getBookingDTO().getPeople_amount().equals(3)
        ){return true;}
        return false;
    }

    public Boolean ValidatePaymentMethodAndDues(PaymentMethodDTO paymentMethodDTO)
    {
        if (paymentMethodDTO.getType().toUpperCase().equals("CREDIT")
                && paymentMethodDTO.getDues() >= 1
        ) {return true; }
        else if (paymentMethodDTO.getType().toUpperCase().equals("DEBIT")
                && paymentMethodDTO.getDues() == 1)
        {return true;}
        else return false;
    }

    public Double CalculateInterest(PaymentMethodDTO paymentMethodDTO)
    {

        if (paymentMethodDTO.getType().toUpperCase().equals("DEBIT"))
        {               return 0.0;     }
        else if (paymentMethodDTO.getType().toUpperCase().equals("CREDIT"))
        {
            if (paymentMethodDTO.getDues() > 1 && paymentMethodDTO.getDues() <= 3 )
            {return 5.0;}
            else if (paymentMethodDTO.getDues() > 3 && paymentMethodDTO.getDues() <= 6)
            {return 10.0;}
        }
        return 15.0;  //Este seria el interes por mas cuotas que 6 (No definido en la Doc).
    }





}
