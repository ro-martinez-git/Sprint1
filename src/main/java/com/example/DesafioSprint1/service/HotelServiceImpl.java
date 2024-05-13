package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.repository.IHotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
