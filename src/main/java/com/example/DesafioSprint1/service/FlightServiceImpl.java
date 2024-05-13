package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.FlightDTO;
import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.repository.IFlightRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
