package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IFlightReservationRepository extends JpaRepository<FlightReservation, Long> {

}
