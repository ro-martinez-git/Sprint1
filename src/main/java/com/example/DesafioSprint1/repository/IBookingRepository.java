package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Booking;
import com.example.DesafioSprint1.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByHotelCode(String hotelCode);

}
