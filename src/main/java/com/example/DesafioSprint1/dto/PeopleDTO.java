package com.example.DesafioSprint1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleDTO {

        private Integer dni;
        private String name;
        @JsonProperty("last_name")
        private String lastName;
        @JsonProperty("birth_date")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate birthDate;
        @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$",message = "Por favor ingrese un e-mail válido")
        private String email;


}
