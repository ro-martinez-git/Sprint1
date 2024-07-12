package com.example.DesafioSprint1.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Top3ClienteResponseDTO{

    @JsonProperty("top_number")
    private Integer topNumber;
    private Integer year;
    @JsonProperty("booking_quantity")
    private Integer bookinQuantity;
    @JsonProperty("total_amount")
    private Double totalAmount;
    @JsonProperty("cliente_id")
    private Long clienteId;
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("client_lastname")
    private String clientLastname;

}