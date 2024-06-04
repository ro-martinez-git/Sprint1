package com.example.DesafioSprint1.dto;

import com.example.DesafioSprint1.dto.PeopleDTO;
import com.example.DesafioSprint1.dto.Request.PaymentMethodDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class BookingDTO {

        @NotEmpty(message = "El destino elegido no existe")
        private String destination;
        @JsonProperty("date_from")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate dateFrom;
        @JsonProperty("date_to")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate dateTo;
        @JsonProperty("hotel_code")
        private String hotelCode;
        @Positive(message = "La cantidad de personas debe ser un valor numérico")
        private Integer people_amount;
        @JsonProperty("room_type")
        private String roomType;
        @JsonProperty("people")
        private @Valid List<PeopleDTO> peopleList;
        @JsonProperty("payment_method")
        private @Valid PaymentMethodDTO paymentMethodDTO;


}
