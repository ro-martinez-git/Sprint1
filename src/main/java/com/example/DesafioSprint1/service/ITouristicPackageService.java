package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.Response.TouristicPackageResponseDTO;
import com.example.DesafioSprint1.dto.TouristicPackageDTO;

import java.util.List;

public interface ITouristicPackageService {

    void createTouristicPackage(TouristicPackageDTO dto);
    void updateTouristicPackage(int packageNumber, TouristicPackageDTO request);
    void deleteTouristicPackage(int packageNumber);
    List<TouristicPackageResponseDTO> getAllTouristicPackages();
}
