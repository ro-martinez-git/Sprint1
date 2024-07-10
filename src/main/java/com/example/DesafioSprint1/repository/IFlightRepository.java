package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IFlightRepository extends JpaRepository<Flight, Long> {

    Optional<List<Flight>>findByFlightNumber(String flightNumber);

}
