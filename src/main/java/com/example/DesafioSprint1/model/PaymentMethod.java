package com.example.DesafioSprint1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String  type;
    @JsonProperty("number_card")
    @Column(name = "number_card")
    private String  numberCard;
    @Column(name = "dues")
    private Integer dues;

    @OneToOne(mappedBy = "paymentMethod")
    private Booking booking;

    @OneToOne(mappedBy = "paymentMethod")
    private FlightReservation flightReservation;

}
