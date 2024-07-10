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
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destination")
    private String destination;
    @JsonProperty("date_from")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_from")
    private LocalDate dateFrom;
    @JsonProperty("date_to")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_to")
    private LocalDate dateTo;
    @JsonProperty("hotel_code")
    @Column(name = "hotel_code")
    private String hotelCode;
    @Column(name = "people_amount")
    private Integer people_amount;
    @JsonProperty("room_type")
    @Column(name = "room_type")
    private String roomType;

    @ManyToMany()
    @JoinTable(name = "booking_people"
            , joinColumns = @JoinColumn(name = "bookings_id")
            , inverseJoinColumns = @JoinColumn(name = "people_id")
    )
    private List<People> peopleList;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_methods_id")
    private PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "booking")
    private Hotel hotel;

    @ManyToOne
    @JsonProperty("user_name")
    @JoinColumn(name = "clientes_id")
    private Cliente cliente;


}
