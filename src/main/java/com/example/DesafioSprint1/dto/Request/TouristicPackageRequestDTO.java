package com.example.DesafioSprint1.dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouristicPackageRequestDTO {
    private int packageNumber;
    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate;

    private int clientId;
}
