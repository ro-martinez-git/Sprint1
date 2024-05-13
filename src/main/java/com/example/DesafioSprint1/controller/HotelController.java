package com.example.DesafioSprint1.controller;

import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HotelController {

    //Inyectar la dependencia
    @Autowired
    private IHotelService hotelService;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> listHotels() {
        return new ResponseEntity<>(hotelService.listHotels(), HttpStatus.OK);
    }

}

