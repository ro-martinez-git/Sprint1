package com.example.DesafioSprint1.dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class TouristicPackageResponseDTO {

    @JsonProperty("package_number")
    private int packageNumber;

    private String name;

    @JsonProperty("creation_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate;

    @JsonProperty("client_id")
    private int clientId;

    @JsonProperty("total_cost")
    private Double totalCost;
}
