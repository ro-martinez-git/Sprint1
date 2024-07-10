package com.example.DesafioSprint1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "flight_reservations")
public class FlightReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destination")
    private String destination;
    @Column(name = "origin")
    private String origin;
    @JsonProperty("date_from")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_from")
    private LocalDate dateFrom;
    @JsonProperty("date_to")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_to")
    private LocalDate dateTo;
    @JsonProperty("flight_number")
    @Column(name = "flight_number")
    private String flightNumber;
    @Column(name = "seat_type")
    private String seatType;
    @JsonProperty("amount")
    @Column(name = "amount")
    private Double amount;

    @ManyToMany
    @JoinTable(name = "flight_reservations_people"
            , joinColumns = @JoinColumn(name = "flight_reservations_id")
            , inverseJoinColumns = @JoinColumn(name = "people_id")
    )
    private List<People> peopleList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "flightReservation")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "clientes_id")
    private Cliente cliente;


}
