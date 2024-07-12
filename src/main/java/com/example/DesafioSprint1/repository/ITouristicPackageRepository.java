package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.TouristicPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITouristicPackageRepository extends JpaRepository<TouristicPackage, Integer> {
    TouristicPackage findByPackageNumber(int packageNumber);
}
