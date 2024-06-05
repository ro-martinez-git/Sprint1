package com.example.DesafioSprint1.dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDTO {
        private String  type;
        @JsonProperty("number_card")
        private String  numberCard;
        @Positive(message = "La catidad de cuotas debe se al menos 1.")
        private Integer dues;


}
