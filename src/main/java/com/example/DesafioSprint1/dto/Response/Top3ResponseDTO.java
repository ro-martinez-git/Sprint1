package com.example.DesafioSprint1.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Top3ResponseDTO {


    List<Top3ClienteResponseDTO> clientes;

}