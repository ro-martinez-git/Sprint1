package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.*;
import com.example.DesafioSprint1.dto.Request.FlightReservationRequestDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.example.DesafioSprint1.dto.Response.FlightReservationResponseDTO;
import com.example.DesafioSprint1.dto.Response.StatusDTO;
import com.example.DesafioSprint1.exceptions.BookingRegistrationException;
import com.example.DesafioSprint1.exceptions.EmptyFlightReservationException;
import com.example.DesafioSprint1.exceptions.InvalidDateFromException;
import com.example.DesafioSprint1.exceptions.InvalidPaymentDebitDues;
import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.repository.IFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightReservationServiceImpl implements IFlightReservationService {

    @Autowired
    private IFlightRepository flightRepository;

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


        }


    }

