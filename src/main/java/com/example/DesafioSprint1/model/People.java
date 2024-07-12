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
@Table(name = "people")
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni")
    private Integer dni;
    @Column(name = "name")
    private String name;
    @JsonProperty("last_name")
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birth_date")
    @JsonProperty("birth_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    @Column(name = "email")
    private String email;

    @ManyToMany(mappedBy = "peopleList")
    private List<Booking> bookingList;

    @ManyToMany(mappedBy = "peopleList")
    private List<FlightReservation> flightReservationList;


}
