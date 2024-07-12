package com.example.DesafioSprint1.service;


import com.example.DesafioSprint1.dto.HotelDTO;
import com.example.DesafioSprint1.dto.Request.HotelRequestDTO;
import com.example.DesafioSprint1.dto.RespuestaDTO;
import com.example.DesafioSprint1.exceptions.*;
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
        List<Hotel> hotelList = hotelRepository.findAll();

        List<Hotel> newHotelList = hotelList.stream()
                .filter(hotel -> hotel.getHotelCode().equals(requestDTO.getHotelCode()))
                .filter(hotel -> hotel.getRoomType().equals(requestDTO.getRoomType()))
               .toList();

        if (!newHotelList.isEmpty()) {
            throw new HotelExistException();
        }

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
    public RespuestaDTO actualizarHotel(Long id, HotelRequestDTO hotelRequestDTO) {
        Optional<Hotel> hotelRepo = hotelRepository.findById(id);
        if (hotelRepo.isPresent()) {
            Hotel hotelModificado = modelMapper.map(hotelRequestDTO, Hotel.class);
            Hotel hotel = hotelRepo.get();
            hotelModificado.setId(hotel.getId());
            hotel = hotelModificado;
            hotelRepository.save(hotel);

            return new RespuestaDTO("Hotel modificado con exito");
        } else {
            throw new HotelNotFoundException();
        }
    }
}
