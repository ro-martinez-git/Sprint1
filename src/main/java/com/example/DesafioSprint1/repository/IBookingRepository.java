package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Booking;
import com.example.DesafioSprint1.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

    
}
