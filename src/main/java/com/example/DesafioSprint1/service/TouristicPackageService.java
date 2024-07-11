package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.TouristicPackageDTO;
import com.example.DesafioSprint1.dto.Response.TouristicPackageResponseDTO;
import com.example.DesafioSprint1.model.Booking;
import com.example.DesafioSprint1.model.FlightReservation;
import com.example.DesafioSprint1.model.TouristicPackage;
import com.example.DesafioSprint1.repository.IBookingRepository;
import com.example.DesafioSprint1.repository.IClienteRepository;
import com.example.DesafioSprint1.repository.IFlightReservationRepository;
import com.example.DesafioSprint1.repository.ITouristicPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TouristicPackageService {

    @Autowired
    private IFlightReservationRepository flightReservationRepository;

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private ITouristicPackageRepository touristicPackageRepository;

    public void createTouristicPackage(TouristicPackageDTO dto) {
        TouristicPackage touristicPackage = new TouristicPackage();
        mapRequestToTouristicPackage(dto, touristicPackage);
        touristicPackageRepository.save(touristicPackage);
    }

    public void updateTouristicPackage(int packageNumber, TouristicPackageDTO request) {
        TouristicPackage touristicPackage = touristicPackageRepository.findByPackageNumber(packageNumber);
        if (touristicPackage == null) {
            throw new RuntimeException("Paquete turistico no encontrado con numero: " + packageNumber);
        }
        mapRequestToTouristicPackage(request, touristicPackage);
        touristicPackageRepository.save(touristicPackage);
    }

    public void deleteTouristicPackage(int packageNumber) {
        TouristicPackage touristicPackage = touristicPackageRepository.findByPackageNumber(packageNumber);
        if (touristicPackage == null) {
            throw new RuntimeException("Paquete turistico no encontrado con numero: " + packageNumber);
        }
        touristicPackageRepository.delete(touristicPackage);
    }

    public List<TouristicPackageResponseDTO> getAllTouristicPackages() {
        List<TouristicPackage> touristicPackages = touristicPackageRepository.findAll();
        return touristicPackages.stream().map(this::mapTouristicPackageToResponseDTO).collect(Collectors.toList());
    }

    private TouristicPackageResponseDTO mapTouristicPackageToResponseDTO(TouristicPackage touristicPackage) {
        TouristicPackageResponseDTO responseDTO = new TouristicPackageResponseDTO();
        responseDTO.setPackageNumber(touristicPackage.getPackageNumber());
        responseDTO.setName(touristicPackage.getName());
        responseDTO.setCreationDate(touristicPackage.getCreationDate());
        responseDTO.setClientId(Math.toIntExact(touristicPackage.getCliente().getId()));
        responseDTO.setTotalCost(touristicPackage.getTotalCost());
        return responseDTO;
    }

    private TouristicPackage mapRequestToTouristicPackage(TouristicPackageDTO request, TouristicPackage touristicPackage) {
        TouristicPackage TouristicPackage = new TouristicPackage();



        if (request.getPackageNumber() > 0) {
            touristicPackage.setPackageNumber(request.getPackageNumber());
        }


        touristicPackage.setName(request.getName());
        touristicPackage.setCreationDate(request.getCreationDate());
        touristicPackage.setCliente(clienteRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getClientId())));

        if (request.getBookingsOrReservations() != null) {
            List<FlightReservation> flightReservations = new ArrayList<>();
            List<Booking> hotelBookings = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : request.getBookingsOrReservations().entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();

                if (key.equals("flight_reservation")) {
                    FlightReservation flightReservation = flightReservationRepository.findById(Long.valueOf(value))
                            .orElseThrow(() -> new RuntimeException("Reserva de vuelo no encontrada con ID: " + request.getBookingsOrReservations().get("flight_reservation(flight_id)")));
                    flightReservations.add(flightReservation);
                } else if (key.equals("booking")) {
                    Booking booking = bookingRepository.findById(Long.valueOf(value))
                            .orElseThrow(() -> new RuntimeException("Reserva de hotel no encontrada con ID: " + request.getBookingsOrReservations().get("booking(hotel_id)")));
                    hotelBookings.add(booking);
                }
            }

            TouristicPackage.setFlightReservations(flightReservations);
            TouristicPackage.setBookings(hotelBookings);
        }

        return touristicPackage;
    }
}