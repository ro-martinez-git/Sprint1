package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.exceptions.DateRangeFrom;
import com.example.DesafioSprint1.exceptions.ReservationInexistentException;
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
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private static final HotelDTO HotelDTO1 = new HotelDTO("Cataratas Hotel","Puerto Iguazú", "Doble", LocalDate.of(2025, 02, 10), LocalDate.of(2025, 03, 20), "NO");
    private static final HotelDTO HotelDTO2 = new HotelDTO("Cataratas Hotel 2", "Puerto Iguazú", "Triple", LocalDate.of(2025, 02, 10), LocalDate.of(2025, 03, 23), "NO");
    private static final Hotel Hotel1 = new Hotel ("CH-0002", "Cataratas Hotel", "Puerto Iguazú", "Doble", 6300.0, LocalDate.of(2025, 02, 10),LocalDate.of(2025, 03, 20), "NO");
    private static final Hotel Hotel2 = new Hotel ("CH-0002", "Cataratas Hotel 2", "Puerto Iguazú", "Triple", 6300.0, LocalDate.of(2025, 02, 10),LocalDate.of(2025, 03, 23), "NO");

    @Test
    @DisplayName("Test FindAll OK")
    //public List<HotelDTO> listHotels()

    void listHotels() {

        //ARRANGE
        List<Hotel>listaHotel = List.of(Hotel1);
        List<HotelDTO> listaHotelEsperada = List.of(HotelDTO1);

        //ACT
        Mockito.when(hotelRepository.findAll()).thenReturn(listaHotel);
        List<HotelDTO> listaHotelObtenida = hotelService.listHotels();

        //ASSERT
        Assertions.assertEquals(listaHotelEsperada, listaHotelObtenida);

    }

    // List<HotelDTO> availableHotels(LocalDate dateFrom, LocalDate dateTo, String destination)
    @Test
    @DisplayName("Test availableHotels OK")
    void availableHotels() {
        //ARRANGE
        List<HotelDTO> listaHotelEsperada = List.of(HotelDTO1);
        List<Hotel>listaHotel = List.of(Hotel1);

        LocalDate dateFromEntrada = LocalDate.of(2025, 02, 10);
        LocalDate dateToEntrada= LocalDate.of(2025, 03, 20);
        String destinationEntrada= "Puerto Iguazú";

        //ACT
        Mockito.when(hotelRepository.findAll()).thenReturn(listaHotel);
        List<HotelDTO> listaHotelObtenida = hotelService.availableHotels(dateFromEntrada, dateToEntrada, destinationEntrada);

        //ASSERT
        Assertions.assertEquals(listaHotelEsperada, listaHotelObtenida);

    }
    @Test
    @DisplayName("Test availableHotels Fail 1")
    void availableHotelsFail1() {
        //ARR
        List<Hotel> listaVacia = new ArrayList<>();
        LocalDate dateFromEntrada = LocalDate.of(2025, 02, 10);
        LocalDate dateToEntrada= LocalDate.of(2025, 03, 20);
        String destinationEntrada= "Puerto Iguazú";
        //ACT
        Mockito.when(hotelRepository.findAll()).thenReturn(listaVacia);

        //ASSERT
        Assertions.assertThrows(ReservationInexistentException.class,
                () -> hotelService.availableHotels(dateFromEntrada, dateToEntrada, destinationEntrada));

    }
    /* Test Unit para validar hoteles disponibles entre el rango de fechas. DateFrom posterior a DateFrom del hotel y lo mismo con DateTo */
    @Test
    @DisplayName("Test availableHotelsRange OK")
    void availableHotelsRange() {
        //ARRANGE
        List<HotelDTO> listaHotelEsperada = List.of(HotelDTO2);
        List<Hotel>listaHotel = List.of(Hotel2);

        LocalDate dateFromEntrada = LocalDate.of(2025, 02, 11);
        LocalDate dateToEntrada= LocalDate.of(2025, 03, 15);
        String destinationEntrada= "Puerto Iguazú";

        //ACT
        Mockito.when(hotelRepository.findAll()).thenReturn(listaHotel);
        List<HotelDTO> listaHotelObtenida = hotelService.availableHotels(dateFromEntrada, dateToEntrada, destinationEntrada);

        //ASSERT
        Assertions.assertEquals(listaHotelEsperada, listaHotelObtenida);

    }

    @Test
    @DisplayName("Test availableHotels Fail 2")
    void availableHotelsFail2() {

        LocalDate dateFromEntrada = LocalDate.of(2026, 02, 10);
        LocalDate dateToEntrada= LocalDate.of(2025, 03, 20);
        String destinationEntrada= "Puerto Iguazú";

        //ASSERT
        Assertions.assertThrows(DateRangeFrom.class,
                () -> hotelService.availableHotels(dateFromEntrada, dateToEntrada, destinationEntrada));

    }

}