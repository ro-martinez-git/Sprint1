package com.example.DesafioSprint1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class TouristicPackageDTO {


    @JsonProperty("package_number")
    private int packageNumber;

    @NotEmpty(message = "El nombre del paquete no puede estar vacio")
    private String name;

    @JsonProperty("creation_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate;

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("bookings_or_reservations")
    private Map<String, Integer> bookingsOrReservations;
}