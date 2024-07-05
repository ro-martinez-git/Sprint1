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
@Table(name = "hotels")
public class Hotel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("hotel_code")
    @Column(name = "hotel_code")
    private String hotelCode;
    @JsonProperty("hotel_name")
    @Column(name = "hotel_name")
    private String hotelName;
    @Column(name = "destination")
    private String destination;
    @JsonProperty("room_type")
    @Column(name = "room_type")
    private String roomType;
    @Column(name = "amount")
    private Double amount;
    @JsonProperty("date_from")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_from")
    private LocalDate dateFrom;
    @JsonProperty("date_to")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_to")
    private LocalDate dateTo;
    @Column(name = "reserved")
    private String reserved;

    @OneToOne
    @JoinColumn(name = "bookings_id")
    private Booking booking;



}
