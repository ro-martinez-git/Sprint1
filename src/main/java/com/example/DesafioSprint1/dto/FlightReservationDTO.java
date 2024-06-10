package com.example.DesafioSprint1.dto;

import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightReservationDTO {
    @JsonProperty("date_from")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateFrom;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("date_to")
    private LocalDate dateTo;
    private String origin;
    @NotEmpty(message = "El destino elegido no existe")
    private String destination;
    @JsonProperty("flight_number")
    private String flightNumber;
    @Positive(message = "La cantidad de asientos debe ser un valor numérico")
    private int seats;
    @JsonProperty("seat_type")
    private String seatType;
    @JsonProperty("payment_method")
    public PaymentMethodDTO paymentMethod;
    private List<@Valid PeopleDTO> people;

}
