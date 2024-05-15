package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.ErrorDTO;
import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.exceptions.ReservationInexistentException;
import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.repository.IHotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Service
public class HotelServiceImpl implements IHotelService{
ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private IHotelRepository hotelRepository;

    @Override
    public List<HotelDTO> listHotels() {
        List<Hotel> hotelList = hotelRepository.findAll();
        return hotelList.stream()
                .map(hotel -> modelMapper
                        .map(hotel, HotelDTO.class))
                .toList();
    }

    @Override
    public List<HotelDTO> availableHotels(LocalDate dateFrom, LocalDate dateTo, String destination) {
       List<HotelDTO> availableHotels = listHotels();
       availableHotels= availableHotels.stream()
               .filter(hotelDTO -> hotelDTO.getDestination().equals(destination))
               .filter(hotelDTO -> dateFrom.isAfter(hotelDTO.getDateFrom()) || dateFrom.equals(hotelDTO.getDateFrom()))
               .filter(hotelDTO -> dateTo.isBefore(hotelDTO.getDateTo()) || dateTo.equals(hotelDTO.getDateTo()))
               .filter(hotelDTO -> hotelDTO.getReserved().equals("NO"))
               .collect(Collectors.toList());
       if(availableHotels.isEmpty()){
           throw new ReservationInexistentException();
       }
       return availableHotels;

    }


}
