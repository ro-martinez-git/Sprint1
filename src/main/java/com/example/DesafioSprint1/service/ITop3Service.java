package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.Response.Top3ResponseDTO;

public interface ITop3Service {

    Top3ResponseDTO getTop3ClientsByReservations(Integer year);
}
