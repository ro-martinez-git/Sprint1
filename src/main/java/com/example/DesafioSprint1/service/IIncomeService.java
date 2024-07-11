package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.Response.IncomeResponseDTO;
import com.example.DesafioSprint1.dto.Response.IncomeResponseMonthDTO;

import java.time.LocalDate;

public interface IIncomeService {

    IncomeResponseDTO getIncomeByDate(LocalDate date);
    IncomeResponseMonthDTO getIncomeByMonth(Integer month, Integer year);

}
