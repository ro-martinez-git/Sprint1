package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.RespuestaDTO;
import com.example.DesafioSprint1.exceptions.DateRangeFrom;
import com.example.DesafioSprint1.exceptions.FlightExistException;
import com.example.DesafioSprint1.exceptions.FlightNoExistException;
import com.example.DesafioSprint1.exceptions.FlightNotFoundException;
import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.repository.IFlightRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements IFlightService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private IFlightRepository flightRepository;

    @Override
    public List<FlightDTO> listFlights() {
        List<Flight> flightList = flightRepository.findAll();
        return flightList.stream()
                .map(flight -> modelMapper
                        .map(flight, FlightDTO.class))
                .toList();
    }

    @Override
    public List<FlightDTO> findFlightsByDateAndRoute(LocalDate dateFrom, LocalDate dateTo, String origin, String destination) {
        if (dateFrom.isAfter(dateTo)) {
            throw new DateRangeFrom();
        }
        if(dateFrom== null || dateTo == null || origin == null || destination == null){
            throw new FlightNotFoundException();
        }
        List<Flight> flightList = flightRepository.findAll();

        // filtro por origen y destino

        List <Flight> flightsRuta = flightList.stream()
                .filter(flight -> flight.getOrigin().equals(origin) && flight.getDestination().equals(destination))
                .toList();

        // filtro por fecha

        List <Flight> flightsFecha = flightsRuta.stream()
                .filter(flight -> flight.getDateFrom().equals(dateFrom) && flight.getDateTo().equals(dateTo))
                .toList();
        if(flightsFecha.isEmpty()){
            throw new FlightNotFoundException();
        }

        return flightsFecha.stream()
                .map(flight -> modelMapper
                        .map(flight, FlightDTO.class))
                .toList();


    }

    @Override
    public RespuestaDTO crear(FlightDTO requestflightDTO) {
        List<Flight> listaVuelos = flightRepository.findAll();

        List<Flight> vuelosFiltrados = listaVuelos.stream()
                .filter(flight -> flight.getFlightNumber().equals(requestflightDTO.getFlightNumber())                    )
                .filter(flight -> flight.getSeatType().equals(requestflightDTO.getSeatType())                    )
                .toList();

        if (!vuelosFiltrados.isEmpty()) {
            throw new FlightExistException();
        }
        Flight flight = modelMapper.map(requestflightDTO, Flight.class);
        flightRepository.save(flight);
        return new RespuestaDTO("Vuelo dado de alta correctamente");
    }

    @Override
    public RespuestaDTO actualizar(Long Id, FlightDTO flightDTO) {
        Optional<Flight> optionalFlight = flightRepository.findById(Id);
        if (optionalFlight.isPresent()) {
            Flight fligthModificado = modelMapper.map(flightDTO, Flight.class);
            Flight vuelo = optionalFlight.get();
            fligthModificado.setId(vuelo.getId());
            vuelo = fligthModificado;
            flightRepository.save(vuelo);

            return new RespuestaDTO("Vuelo modificado correctamente");
        }
        else {
            throw new FlightNoExistException();
        }
    }
    @Override
    public RespuestaDTO borrar(Long Id) {
        Optional<Flight> optionalFlight = flightRepository.findById(Id);
        if (optionalFlight.isEmpty()) {
            throw new FlightNoExistException();
        }
        Flight flightEncontrado = optionalFlight.get();
        flightRepository.delete(flightEncontrado);

        return new RespuestaDTO("Vuelo con código " + Id + " borrado correctamente");
    }
}
