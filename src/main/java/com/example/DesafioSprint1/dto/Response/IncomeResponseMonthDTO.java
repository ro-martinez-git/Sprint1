package com.example.DesafioSprint1.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"month", "year", "total_income"})
public class IncomeResponseMonthDTO {


        @JsonProperty("month")
        private Integer month;
        @JsonProperty("year")
        private Integer year;
        @JsonProperty("total_income")
        private Double totalIncome;


}
