package com.example.DesafioSprint1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "touristic_packages")
public class TouristicPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("package_number")
    private int packageNumber;

    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Cliente cliente;

    @ManyToMany
    @JoinTable(name = "touristic_package_flights",
            joinColumns = @JoinColumn(name = "touristic_package_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_reservations"))
    private List<FlightReservation> flightReservations = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "touristic_package_bookings",
            joinColumns = @JoinColumn(name = "touristic_package_id"),
            inverseJoinColumns = @JoinColumn(name = "bookings"))
    private List<Booking> bookings = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_method")
    private PaymentMethod paymentMethod;

    private Double totalCost;

    public void calculateTotalCost() {
        double flightCost = flightReservations.stream().mapToDouble(FlightReservation::getAmount).sum();
        double hotelCost = bookings.stream().mapToDouble(Booking::getAmount).sum();
        double total = flightCost + hotelCost;
        this.totalCost = total - (total * 0.1); // Aplica el 10% de descuento
    }
}