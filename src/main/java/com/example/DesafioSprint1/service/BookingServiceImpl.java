package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.BookingDTO;
import com.example.DesafioSprint1.dto.PeopleDTO;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.dto.Response.StatusDTO;
import com.example.DesafioSprint1.dto.RespuestaDTO;
import com.example.DesafioSprint1.exceptions.*;
import com.example.DesafioSprint1.model.*;
import com.example.DesafioSprint1.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements IBookingService{

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private IHotelRepository hotelRepository;

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IPeopleRepository peopleRepository;

    @Autowired
    IClienteRepository clienteRepository;

    @Autowired
    IPaymentMethodRepository paymentMethodRepository;

    @Override
    public BookingResponseDTO makeBooking(BookingRequestDTO bookingRequestDTO) throws Exception {
        if (bookingRequestDTO.getBookingDTO().getDateFrom().isAfter(bookingRequestDTO.getBookingDTO().getDateTo())
                || bookingRequestDTO.getBookingDTO().getDateFrom().isEqual(bookingRequestDTO.getBookingDTO().getDateTo())) {
            throw new InvalidDateFromException();
        }

        if (!ValidateRoomType(bookingRequestDTO)) {
            throw new InvalidRoomTypeException();
        }

        if (!ValidatePaymentMethodAndDues(bookingRequestDTO.getBookingDTO().getPaymentMethodDTO())) {
            if (bookingRequestDTO.getBookingDTO().getPaymentMethodDTO().getType().toUpperCase().equals("DEBIT")) {
                throw new InvalidPaymentDebitDues();
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
        if (hotels.isEmpty()) {
            throw new BookingRegistrationException();
        }
        //Valida que no haya otro con las mismas caracteristicas
        if (!validaReservaYaExistente(bookingRequestDTO)) { throw new HotelBookingAlreadyRegisteredException(); }

        //Valida que no este ya reservado el Hotel
        hotels = hotels.stream().filter(hotel -> hotel.getReserved().equals("NO")).toList();
        if (hotels.isEmpty()) {            throw new HotelNotAvailableException();        }

        Double amount = CalculateBookingAmount(days, hotels.get(0).getAmount(), bookingRequestDTO.getBookingDTO().getPeople_amount());
        Double total = ((amount * interest / 100) + amount);
        answer.setUserName(bookingRequestDTO.getUserName());
        answer.setAmount(amount);
        answer.setInterest(interest);
        answer.setTotal(total);
        answer.setBookingDTO(bookingRequestDTO.getBookingDTO());
        answer.setStatusDTO(new StatusDTO("El proceso termino satisfactoriamente", 201));

        //Se agrega para integrar con JPA
        Booking booking = modelMapper.map(bookingRequestDTO.getBookingDTO(), Booking.class);
        Cliente cliente = clienteRepository.findByUsername(bookingRequestDTO.getUserName()).get();
        booking.setCliente(cliente);
        booking.setHotel(hotelRepository.findByHotelCode(bookingRequestDTO.getBookingDTO().getHotelCode()));
        booking.setPaymentMethod(
                modelMapper.map(bookingRequestDTO.getBookingDTO().getPaymentMethodDTO(), PaymentMethod.class)
        );
        List<People> peopleList =  new ArrayList<>();
        for( PeopleDTO peopleDTO : bookingRequestDTO.getBookingDTO().getPeopleList())
        {   People people = modelMapper.map(peopleDTO, People.class);
            peopleList.add(people);
        }
        List<People> savedPeopleList = new ArrayList<>();
        for (People person : peopleList) {
            Optional<People> existingPerson = peopleRepository.findByDni(person.getDni());
            if (existingPerson.isPresent()) {                savedPeopleList.add(existingPerson.get());
            } else {
                People savedPerson = peopleRepository.save(person);
                savedPeopleList.add(savedPerson);
            }

        }         booking.setPeopleList(savedPeopleList);
        booking.getHotel().setReserved("SI");
        bookingRepository.save(booking);

        return answer;

    }

    @Override
    public BookingResponseDTO saveBooking(BookingRequestDTO bookingRequestDTO, Long id) {
        Optional<Booking> oBooking = bookingRepository.findById(id);
        if (oBooking.isEmpty()) {
            throw new BookingRegistrationException();
        }
        Booking booking = oBooking.get();
        Cliente cliente = clienteRepository.findByUsername(bookingRequestDTO.getUserName()).get();
        booking.setCliente(cliente);
        booking.setDateFrom(bookingRequestDTO.getBookingDTO().getDateFrom());
        booking.setDateTo(bookingRequestDTO.getBookingDTO().getDateTo());
        booking.setRoomType(bookingRequestDTO.getBookingDTO().getRoomType());
        booking.setPeople_amount(bookingRequestDTO.getBookingDTO().getPeople_amount());
        booking.setHotel(hotelRepository.findByHotelCode(bookingRequestDTO.getBookingDTO().getHotelCode()));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(booking.getPaymentMethod().getId()).get();
        paymentMethod.setDues(bookingRequestDTO.getBookingDTO().getPaymentMethodDTO().getDues());
        paymentMethod.setType(bookingRequestDTO.getBookingDTO().getPaymentMethodDTO().getType());
        paymentMethod.setNumberCard(bookingRequestDTO.getBookingDTO().getPaymentMethodDTO().getNumberCard());
        paymentMethodRepository.save(paymentMethod);
        booking.setPaymentMethod(paymentMethod);

        bookingRepository.save(booking);

        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO()    ;
        bookingResponseDTO.setUserName(booking.getCliente().getUsername());
        bookingResponseDTO.setBookingDTO(modelMapper.map(booking,BookingDTO.class));
        bookingResponseDTO.getBookingDTO().setPaymentMethodDTO(modelMapper.map(booking.getPaymentMethod(),PaymentMethodDTO.class));

        Long days = bookingRequestDTO.getBookingDTO().getDateTo().toEpochDay() - bookingRequestDTO.getBookingDTO().getDateFrom().toEpochDay();
        bookingResponseDTO.setAmount(CalculateBookingAmount(days , booking.getHotel().getAmount(), booking.getPeople_amount()));
        bookingResponseDTO.setInterest(CalculateInterest(bookingRequestDTO.getBookingDTO().getPaymentMethodDTO())        );
        bookingResponseDTO.setTotal(    ((bookingResponseDTO.getAmount() * bookingResponseDTO.getInterest() / 100) + bookingResponseDTO.getAmount() )    );
        bookingResponseDTO.setStatusDTO(new StatusDTO("El proceso termino satisfactoriamente", 200));
        return bookingResponseDTO;

    }

    @Override
    public List<BookingDTO> listaBookings() {

        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty() ) {throw  new NoBookingsException(); }
        List<BookingDTO> bookingDTOs = new ArrayList<>();
        BookingDTO bookingDTO = new BookingDTO();
        for (Booking booking : bookings) {
            bookingDTO = modelMapper.map(booking, BookingDTO.class);
            bookingDTO.setPaymentMethodDTO(modelMapper.map(booking.getPaymentMethod(), PaymentMethodDTO.class));
            bookingDTOs.add(bookingDTO);
        }
        return bookingDTOs;
    }

    @Override
    public RespuestaDTO deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            return new RespuestaDTO("No se encontró la reserva con el ID proporcionado");
        }
        Hotel hotel  = booking.getHotel();
        if (hotel != null) {
            hotel.setReserved("NO");
            hotel.setBooking(null);
            hotelRepository.save(hotel);
        }


        bookingRepository.delete(booking);
        return new RespuestaDTO("Reserva dada de baja con exito");
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

    public Double CalculateBookingAmount(Long days , Double unitaryPrice, Integer peopleAmount)
    {
        Double amount = unitaryPrice * days * peopleAmount;
        return amount;

    }

    public Boolean validaReservaYaExistente(BookingRequestDTO bookingRequestDTO)
    {   Booking request = modelMapper.map(bookingRequestDTO.getBookingDTO(), Booking.class);
        List<Booking> obtenido = bookingRepository.findByHotelCode(bookingRequestDTO.getBookingDTO().getHotelCode());
        if(!obtenido.isEmpty()){
            obtenido = obtenido.stream()
                    .filter(bookingRepo -> bookingRepo.getCliente().getUsername().equals(bookingRequestDTO.getUserName())                    )
                    .filter(bookingRepo -> bookingRepo.getDestination().equals(request.getDestination())                    )
                    .filter(bookingRepo -> bookingRepo.getDateFrom().equals(request.getDateFrom()))
                    .filter(bookingRepo -> bookingRepo.getDateTo().equals(request.getDateTo()))
                    .filter(bookingRepo -> bookingRepo.getPeople_amount().equals(request.getPeople_amount()))
                    .filter(bookingRepo -> bookingRepo.getRoomType().equals(request.getRoomType()))
                    .toList();
        }
    if( obtenido.isEmpty()) {return true;}

    return false;
    }



}

