package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.HotelDTO;

import java.time.LocalDate;
import java.util.List;

public interface IHotelService {

    List<HotelDTO> listHotels();
    List<HotelDTO> availableHotels(LocalDate dateFrom, LocalDate dateTo, String destination);
}
