package com.example.DesafioSprint1.service;


import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.dto.Request.HotelRequestDTO;
import com.example.DesafioSprint1.dto.RespuestaDTO;
import com.example.DesafioSprint1.exceptions.DateRangeFrom;
import com.example.DesafioSprint1.exceptions.HotelNotFoundException;
import com.example.DesafioSprint1.exceptions.ReservationInexistentException;
import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.repository.IHotelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements IHotelService{
ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private IHotelRepository hotelRepository;


    @Override
    public List<HotelDTO> listHotels() {
        List<Hotel> hotelList = hotelRepository.findAll();
        return hotelList.stream()
                .map(hotel -> modelMapper
                        .map(hotel, HotelDTO.class))
                .toList();
    }

    @Override
    public List<HotelDTO> availableHotels(LocalDate dateFrom, LocalDate dateTo, String destination) {

        if (dateFrom.isAfter(dateTo)) {
            throw new DateRangeFrom();
        }

       List<HotelDTO> availableHotels = listHotels();
       availableHotels= availableHotels.stream()
               .filter(hotelDTO -> hotelDTO.getDestination().equals(destination))
               .filter(hotelDTO -> dateFrom.isAfter(hotelDTO.getDateFrom()) || dateFrom.equals(hotelDTO.getDateFrom()))
               .filter(hotelDTO -> dateTo.isBefore(hotelDTO.getDateTo()) || dateTo.equals(hotelDTO.getDateTo()))
               .filter(hotelDTO -> hotelDTO.getReserved().equals("NO"))
               .collect(Collectors.toList());
       if(availableHotels.isEmpty()){
           throw new ReservationInexistentException();
       }
       return availableHotels;

    }

    @Override
    public HotelDTO findByHotelCode(String hotelCode) {
        Hotel hotel = hotelRepository.findByHotelCode(hotelCode);
        if(hotel == null) {
            throw new HotelNotFoundException();
        }
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public RespuestaDTO save(HotelRequestDTO requestDTO ) {
        Hotel hotel = modelMapper.map(requestDTO, Hotel.class);
        hotelRepository.save(hotel);
        return new RespuestaDTO("Hotel creado con éxito");
    }

    @Override
    public RespuestaDTO delete(String hotelCode) {
        Hotel hotel = hotelRepository.findByHotelCode(hotelCode);
        if (hotel != null) {
            hotelRepository.delete(hotel);
            return new RespuestaDTO("Hotel eliminado con éxito");

        } else {
            throw  new HotelNotFoundException();
        }

    }

    @Override
    public HotelRequestDTO actualizarHotel(HotelRequestDTO hotelRequestDTO) {
        Hotel hotelRepo = hotelRepository.findByHotelCode(hotelRequestDTO.getHotelCode());
        if(hotelRepo == null){
            throw new HotelNotFoundException();
        }
        Hotel hotel = modelMapper.map(hotelRequestDTO, Hotel.class);
        hotelRepository.save(hotel);
        return hotelRequestDTO ;
    }


}
