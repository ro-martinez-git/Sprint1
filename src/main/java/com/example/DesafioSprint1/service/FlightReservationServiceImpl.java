package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.*;
import com.example.DesafioSprint1.dto.Request.BookingRequestDTO;
import com.example.DesafioSprint1.dto.Request.FlightReservationRequestDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.example.DesafioSprint1.dto.Response.BookingResponseDTO;
import com.example.DesafioSprint1.dto.Response.FlightReservationResponseDTO;
import com.example.DesafioSprint1.dto.Response.StatusDTO;
import com.example.DesafioSprint1.exceptions.*;
import com.example.DesafioSprint1.model.*;
import com.example.DesafioSprint1.repository.*;
import jakarta.persistence.Id;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightReservationServiceImpl implements IFlightReservationService {

    @Autowired
    private IFlightRepository flightRepository;
    @Autowired
    private IFlightReservationRepository flightReservationRepository;
    @Autowired
    private IPeopleRepository peopleRepository;

    @Autowired
    IClienteRepository clienteRepository;

    @Autowired
    IPaymentMethodRepository paymentMethodRepository;

    ModelMapper modelMapper = new ModelMapper();
    @Override
    public FlightReservationResponseDTO reserveFlight(FlightReservationRequestDTO request) {

        if (request.getFlightReservation().getDateFrom().isAfter(request.getFlightReservation().getDateTo())
                || request.getFlightReservation().getDateFrom().isEqual(request.getFlightReservation().getDateTo())) {
            throw new InvalidDateFromException();
        }
        if (request.getUserName() == null) {
            throw new EmptyFlightReservationException();
        }
        if (!ValidatePaymentMethodAndDues(request.getFlightReservation().getPaymentMethod())) {
            if (request.getFlightReservation().getPaymentMethod().getType().toUpperCase().equals("DEBIT")) {
                throw new InvalidPaymentDebitDues();
            }
        }

        // obtener informacion del vuelo desde el servicio
        Flight flight = flightRepository.findAll().stream()
                .filter(f -> f.getFlightNumber().equals(request.getFlightReservation().getFlightNumber()))
                .filter(f -> f.getDateFrom().equals(request.getFlightReservation().getDateFrom()))
                .filter(f -> f.getDateTo().equals(request.getFlightReservation().getDateTo()))
                .filter(f -> f.getOrigin().equals(request.getFlightReservation().getOrigin()))
                .filter(f -> f.getDestination().equals(request.getFlightReservation().getDestination()))
                .filter(f -> f.getSeatType().equals(request.getFlightReservation().getSeatType()))
                .findFirst()
                .orElse(null);

        if (flight == null) {
            throw new BookingRegistrationException();
        }


        if (validateBookingFlightExists(request)) {
            throw new FlightBookingAlreadyRegisteredException();
        }

            // calcular el precio base
            double basePrice = flight.getAmount() * request.getFlightReservation().getSeats();
            // calcular los intereses
            double interest = this.calculateInterest(request.getFlightReservation().getPaymentMethod());
            // calcular el monto total
            double total = basePrice * interest / 100 + basePrice;

            FlightReservationResponseDTO response = new FlightReservationResponseDTO();
            response.setUserName(request.getUserName());
            response.setAmount(basePrice);
            response.setInterest(interest);
            response.setTotal(total);
            response.setFlightReservation(request.getFlightReservation());
            response.setStatusDTO(new StatusDTO("La reserva finalizó satisfactoriamente", 201));

        // calcular el precio base
        double basePrice = flight.getAmount() * request.getFlightReservation().getSeats();
        // calcular los intereses
        double interest = this.calculateInterest(request.getFlightReservation().getPaymentMethod());
        // calcular el monto total
        double total = basePrice * interest / 100 + basePrice;

        FlightReservationResponseDTO response = new FlightReservationResponseDTO();
        response.setUserName(request.getUserName());
        response.setAmount(basePrice);
        response.setInterest(interest);
        response.setTotal(total);
        response.setFlightReservation(request.getFlightReservation());
        response.setStatusDTO(new StatusDTO("La reserva finalizó satisfactoriamente", 201));


        FlightReservation flightReservation = modelMapper.map(request.getFlightReservation(), FlightReservation.class);
        Cliente cliente = clienteRepository.findByUsername(request.getUserName()).get();
        flightReservation.setCliente(cliente);
        flightReservation.setFlight(flightRepository.findByFlightNumber(request.getFlightReservation()
                .getFlightNumber()).get().stream()
                .filter(f -> f.getSeatType().toUpperCase()
                .equals(request.getFlightReservation().getSeatType().toUpperCase())).toList().get(0));
        flightReservation.setPaymentMethod(
                modelMapper.map(request.getFlightReservation().getPaymentMethod(), PaymentMethod.class)
        );
        List<People> peopleList =  new ArrayList<>();
        for( PeopleDTO peopleDTO : request.getFlightReservation().getPeople())
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

        }         flightReservation.setPeopleList(savedPeopleList);
        flightReservation.getFlight().setReserved("SI");
        flightReservation.setAmount(response.getAmount());
        flightReservation.getFlight().setFlightReservation(flightReservation);
        flightReservation.setCreationDate(LocalDate.now());
        flightReservationRepository.save(flightReservation);


            return response;
        }

    @Override
    public FlightReservationResponseDTO saveFlightReservation(FlightReservationRequestDTO flightReservationRequestDTO, Long id) {
        Optional<FlightReservation> oBookingFlight = flightReservationRepository.findById(id);
        if (oBookingFlight.isEmpty()) {
            throw new BookingRegistrationException();
        }
        FlightReservation flightReservation = oBookingFlight.get();

        Cliente cliente = clienteRepository.findByUsername(flightReservationRequestDTO.getUserName()).get();
        flightReservation.setCliente(cliente);
        flightReservation.setDateFrom(flightReservationRequestDTO.getFlightReservation().getDateFrom());
        flightReservation.setDateTo(flightReservationRequestDTO.getFlightReservation().getDateTo());
        flightReservation.setSeatType(flightReservationRequestDTO.getFlightReservation().getSeatType());
        flightReservation.setFlightNumber(flightReservationRequestDTO.getFlightReservation().getFlightNumber());
        flightReservation.setOrigin(flightReservationRequestDTO.getFlightReservation().getOrigin());
        flightReservation.setDestination(flightReservationRequestDTO.getFlightReservation().getDestination());

        PaymentMethod paymentMethod = paymentMethodRepository.findById(flightReservation.getPaymentMethod().getId()).get();
        paymentMethod.setDues(flightReservationRequestDTO.getFlightReservation().getPaymentMethod().getDues());
        paymentMethod.setType(flightReservationRequestDTO.getFlightReservation().getPaymentMethod().getType());
        paymentMethod.setNumberCard(flightReservationRequestDTO.getFlightReservation().getPaymentMethod().getNumberCard());
        paymentMethodRepository.save(paymentMethod);
        flightReservation.setPaymentMethod(paymentMethod);
        flightReservationRepository.save(flightReservation);

        FlightReservationResponseDTO flightReservationResponseDTO = new FlightReservationResponseDTO();
        flightReservationResponseDTO.setUserName(flightReservation.getCliente().getUsername());
        flightReservationResponseDTO.setFlightReservation(modelMapper.map(flightReservation,FlightReservationDTO.class));
        flightReservationResponseDTO.getFlightReservation().setPaymentMethod(modelMapper.map(flightReservation.getPaymentMethod(),PaymentMethodDTO.class));

        Long days = flightReservationRequestDTO.getFlightReservation().getDateTo().toEpochDay() - flightReservationRequestDTO.getFlightReservation().getDateFrom().toEpochDay();
        flightReservationResponseDTO.setAmount(flightReservation.getFlight().getAmount() * flightReservationRequestDTO.getFlightReservation().getSeats());
        flightReservationResponseDTO.setInterest(calculateInterest(flightReservationRequestDTO.getFlightReservation().getPaymentMethod()));
        flightReservationResponseDTO.setTotal(((flightReservationResponseDTO.getAmount() * flightReservationResponseDTO.getInterest() / 100) + flightReservationResponseDTO.getAmount() )    );
        flightReservationResponseDTO.setStatusDTO(new StatusDTO("El proceso termino satisfactoriamente", 200));

        return flightReservationResponseDTO;
    }

    @Override
    public List<FlightReservationDTO> listaReservasFlight() {
        List<FlightReservation> flightReserved = flightReservationRepository.findAll();
            if (flightReserved.isEmpty()) {
                throw new NoBookingsFlightException();
            }
        List<FlightReservationDTO> flightReservas = new ArrayList<FlightReservationDTO>();
            for (FlightReservation flightReservation : flightReserved) {
                FlightReservationDTO flightReservationDTO = modelMapper.map(flightReservation, FlightReservationDTO.class);
                flightReservationDTO.setSeats(flightReservation.getPeopleList().size());
                flightReservas.add(flightReservationDTO);
            }

        return flightReservas;
    }

    @Override
    public RespuestaDTO deleteFlightReservation(Long Id) {
        FlightReservation flightForDelete = flightReservationRepository.findById(Id).orElse(null);

         if (flightForDelete == null) {
            return new RespuestaDTO("No se encontro el Vuelo con el id proporcionado");
        }

         Flight flight = flightForDelete.getFlight();
         if (flight != null) {
             flight.setReserved("NO");
             flight.setFlightReservation(null);
             flightRepository.save(flight);
         }

        flightReservationRepository.delete(flightForDelete);
        return new RespuestaDTO("Reserva dada de baja con exito");
    }

    public Boolean ValidatePaymentMethodAndDues (PaymentMethodDTO paymentMethodDTO)
        {
            if (paymentMethodDTO.getType().toUpperCase().equals("CREDIT")
                    && paymentMethodDTO.getDues() >= 1
            ) {
                return true;
            } else if (paymentMethodDTO.getType().toUpperCase().equals("DEBIT")
                    && paymentMethodDTO.getDues() == 1) {
                return true;
            } else return false;
        }
        private double calculateInterest (PaymentMethodDTO paymentMethodDTO){

            if (paymentMethodDTO.getType().toUpperCase().equals("DEBIT")) {
                return 0.0;
            } else if (paymentMethodDTO.getType().toUpperCase().equals("CREDIT")) {
                if (paymentMethodDTO.getDues() > 1 && paymentMethodDTO.getDues() <= 3) {
                    return 5.0;
                } else if (paymentMethodDTO.getDues() > 3 && paymentMethodDTO.getDues() <= 6) {
                    return 10.0;
                }

        return response;
    }
    public Boolean ValidatePaymentMethodAndDues (PaymentMethodDTO paymentMethodDTO)
    {
        if (paymentMethodDTO.getType().toUpperCase().equals("CREDIT")
                && paymentMethodDTO.getDues() >= 1
        ) {
            return true;
        } else if (paymentMethodDTO.getType().toUpperCase().equals("DEBIT")
                && paymentMethodDTO.getDues() == 1) {
            return true;
        } else return false;
    }
    private double calculateInterest (PaymentMethodDTO paymentMethodDTO){

        if (paymentMethodDTO.getType().toUpperCase().equals("DEBIT")) {
            return 0.0;
        } else if (paymentMethodDTO.getType().toUpperCase().equals("CREDIT")) {
            if (paymentMethodDTO.getDues() > 1 && paymentMethodDTO.getDues() <= 3) {
                return 5.0;
            } else if (paymentMethodDTO.getDues() > 3 && paymentMethodDTO.getDues() <= 6) {
                return 10.0;

            }
        }
        return 15.0;  //Este seria el interes por mas cuotas que 6 (No definido en la Doc).

    public Double CalculateBookingAmount(Long days , Double unitaryPrice, Integer peopleAmount)
    {
        Double amount = unitaryPrice * days * peopleAmount;
        return amount;

    }

    public Boolean validateBookingFlightExists(FlightReservationRequestDTO flightReservationRequestDTO) {
        List<FlightReservation> flightReservations = flightReservationRepository.findAll();
        if (flightReservations.isEmpty()) {
            return false;
        }

        FlightReservation flightReservation = flightReservations.stream()
               .filter(fr -> fr.getFlightNumber().equals(flightReservationRequestDTO.getFlightReservation().getFlightNumber()))
               .filter(fr -> fr.getOrigin().equals(flightReservationRequestDTO.getFlightReservation().getOrigin()))
               .filter(fr -> fr.getDestination().equals(flightReservationRequestDTO.getFlightReservation().getDestination()))
               .filter(fr -> fr.getDateFrom().equals(flightReservationRequestDTO.getFlightReservation().getDateFrom()))
               .filter(fr -> fr.getDateTo().equals(flightReservationRequestDTO.getFlightReservation().getDateTo()))
               .findFirst()
               .orElse(null);

        return flightReservation != null;
    }


}

