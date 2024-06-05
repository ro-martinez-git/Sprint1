package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private static final HotelDTO HotelDTO1 = new HotelDTO("CH-0002", "Puerto Iguazú", "Doble", LocalDate.of(2025, 02, 10), LocalDate.of(2025, 03, 20), "NO");
    private static final HotelDTO HotelDTO2 = new HotelDTO("CH-0003", "Puerto Iguazú", "Triple", LocalDate.of(2025, 02, 10), LocalDate.of(2025, 03, 23), "NO");


    @Test
    @DisplayName("Test FindAll OK")
    //public List<HotelDTO> listHotels()

    void listHotels() {

        //ARRANGE
        List<HotelDTO> listaHotelEsperada = List.of(HotelDTO1, HotelDTO2);

        //ACT
        Mockito.when(hotelService.listHotels()).thenReturn(listaHotelEsperada);
        List<Hotel> listaHotelObtenida = hotelRepository.findAll();

        //ASSERT
        Assertions.assertEquals(listaHotelEsperada, listaHotelObtenida);

    }

    @Test
    void availableHotels() {
    }

    @Test
    void findByHotelCode() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void actualizarHotel() {
    }
}