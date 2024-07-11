package com.example.DesafioSprint1.dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"date", "totalIncome"})
public class IncomeResponseDTO {


        @JsonProperty("date")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate date;

        @JsonProperty("total_income")
        private Double totalIncome;



}
