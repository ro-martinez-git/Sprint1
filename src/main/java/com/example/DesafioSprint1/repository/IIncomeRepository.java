package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface IIncomeRepository extends JpaRepository<Booking, Long> {

@Query("SELECT (SELECT COALESCE(SUM(b.amount), 0) FROM Booking b WHERE b.creationDate = :date) " +
        "+ (SELECT COALESCE(SUM(f.amount), 0) FROM FlightReservation f WHERE f.creationDate = :date) " +
        "FROM Booking b")
    Double getIncomeByDate(LocalDate date);

    @Query("SELECT (SELECT COALESCE(SUM(b.amount), 0) FROM Booking b WHERE YEAR(b.creationDate) = :year AND MONTH(b.creationDate) = :month) " +
            "+ (SELECT COALESCE(SUM(f.amount), 0) FROM FlightReservation f WHERE YEAR(f.creationDate) = :year AND MONTH(f.creationDate) = :month)")
    Double getIncomeByMonth(@Param("month") Integer month, @Param("year") Integer year);

}
