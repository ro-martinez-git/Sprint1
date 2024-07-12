package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IHotelRepository extends JpaRepository<Hotel, Long> {

    Hotel findByHotelCode(String hotelCode);

}
