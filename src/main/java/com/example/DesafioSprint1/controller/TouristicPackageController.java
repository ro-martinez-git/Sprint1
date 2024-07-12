package com.example.DesafioSprint1.controller;

import com.example.DesafioSprint1.dto.TouristicPackageDTO;
import com.example.DesafioSprint1.dto.Response.TouristicPackageResponseDTO;
import com.example.DesafioSprint1.service.TouristicPackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/touristicpackage")
public class TouristicPackageController {

    @Autowired
    private TouristicPackageService touristicPackageService;

    @PostMapping("/new")
    public ResponseEntity<String> createTouristicPackage(@Valid @RequestBody TouristicPackageDTO dto) {
        touristicPackageService.createTouristicPackage(dto);
        return ResponseEntity.ok("Paquete Turistico dado de alta correctamente");
    }

    @PutMapping("/edit")
    public ResponseEntity<String> updateTouristicPackage(
            @Valid @RequestBody TouristicPackageDTO request,
            @RequestParam("packageNumber") int packageNumber) {
        touristicPackageService.updateTouristicPackage(packageNumber, request);
        return ResponseEntity.ok("Paquete turistico actualizado correctamente.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTouristicPackage(@RequestParam("packageNumber") int packageNumber) {
        touristicPackageService.deleteTouristicPackage(packageNumber);
        return ResponseEntity.ok("Paquete turistico eliminado correctamente.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<TouristicPackageResponseDTO>> getAllTouristicPackages() {
        List<TouristicPackageResponseDTO> touristicPackages = touristicPackageService.getAllTouristicPackages();
        return ResponseEntity.ok(touristicPackages);
    }
}
