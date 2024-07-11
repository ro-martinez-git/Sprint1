package com.example.DesafioSprint1.service;


import com.example.DesafioSprint1.dto.Response.IncomeResponseDTO;
import com.example.DesafioSprint1.dto.Response.IncomeResponseMonthDTO;
import com.example.DesafioSprint1.repository.IIncomeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IncomeServiceImpl implements IIncomeService{
ModelMapper modelMapper = new ModelMapper();

    @Autowired
   private IIncomeRepository incomeRepository;

    @Override
    public IncomeResponseDTO getIncomeByDate(LocalDate date) {
        //return new IncomeResponseDTO(date, incomeRepository.getIncomeByDate(date) );
        Double income;
        try { income = incomeRepository.getIncomeByDate(date).doubleValue(); }
        catch (Exception e){            income = 0d;        }
        if(income.isNaN()){            income = 0d;        }
        return new IncomeResponseDTO(date, income );
    }

    @Override
    public IncomeResponseMonthDTO getIncomeByMonth(Integer month, Integer year) {
        return new IncomeResponseMonthDTO(month, year, incomeRepository.getIncomeByMonth(month, year));
    }
}
