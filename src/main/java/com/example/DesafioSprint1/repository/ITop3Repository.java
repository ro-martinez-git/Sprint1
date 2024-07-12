package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITop3Repository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT c.id, c.firstname, COUNT(b.id) AS reservationsCount " +
            "FROM clientes c " +
            "JOIN bookings b ON c.id = b.clientes_id " +
            "GROUP BY c.id, c.firstname " +
            "ORDER BY reservationsCount DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTop3ClientsByReservations();
}