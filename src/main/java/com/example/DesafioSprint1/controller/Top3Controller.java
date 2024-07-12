package com.example.DesafioSprint1.controller;

import com.example.DesafioSprint1.dto.Response.Top3ResponseDTO;
import com.example.DesafioSprint1.service.ITop3Service;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Validated
public class Top3Controller {
    @Autowired
    ITop3Service top3Service;

    public Top3Controller(ITop3Service top3Service) {
        this.top3Service = top3Service;
    }

    @GetMapping("/clients/top-3")
    public ResponseEntity<?> getTop3ClientsByReservations(
            @RequestParam("year")
            @Min(value = 1900, message = "El año debe ser mayor o igual a 1900") Integer year) {

        Top3ResponseDTO topClients = null;
        try {
            topClients = top3Service.getTop3ClientsByReservations(year);
            return new ResponseEntity<>(topClients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}