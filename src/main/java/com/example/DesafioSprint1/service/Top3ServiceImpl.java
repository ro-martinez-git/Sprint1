package com.example.DesafioSprint1.service;

import com.example.DesafioSprint1.dto.Response.Top3ClienteResponseDTO;
import com.example.DesafioSprint1.dto.Response.Top3ResponseDTO;
import com.example.DesafioSprint1.model.Booking;
import com.example.DesafioSprint1.model.Cliente;
import com.example.DesafioSprint1.repository.IBookingRepository;
import com.example.DesafioSprint1.repository.ITop3Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Top3ServiceImpl implements ITop3Service {
    @Autowired
    ITop3Repository top3Repository;
    @Autowired
    IBookingRepository bookingRepository;

    public Top3ResponseDTO getTop3ClientsByReservations(Integer year) {

        List<Booking> listBooking = bookingRepository.findAll().stream()
                .filter(booking -> year.equals(booking.getCreationDate().getYear()))
                .collect(Collectors.toList());

        Map<Cliente, Long> reservationsCountByClient = listBooking.stream()
                .collect(Collectors.groupingBy(Booking::getCliente, Collectors.counting()));
        List<Map.Entry<Cliente, Long>> topClients = reservationsCountByClient.entrySet().stream()
                .sorted(Map.Entry.<Cliente, Long>comparingByValue().reversed())
                .limit(3)
                .toList();

        List<Top3ClienteResponseDTO> top3ClientesDTO = new ArrayList<>();

        int topNumber = 1;

        for (Map.Entry<Cliente, Long> entry : topClients) {
            Cliente cliente = entry.getKey();
            Long bookingQuantity = entry.getValue();

            List<Booking> clienteBookings = listBooking.stream()
                    .filter(booking -> booking.getCliente().equals(cliente))
                    .collect(Collectors.toList());

            // Calcular el total amount para el cliente
            double totalAmount = clienteBookings.stream()
                    .mapToDouble(Booking::getAmount)
                    .sum();


            Top3ClienteResponseDTO dto = new Top3ClienteResponseDTO();
            dto.setTopNumber(topNumber);
            dto.setYear(year);
            dto.setBookinQuantity(bookingQuantity.intValue());
            dto.setTotalAmount(totalAmount);
            dto.setClienteId(cliente.getId());
            dto.setClientName(cliente.getFirstName());
            dto.setClientLastname(cliente.getLastName());

            top3ClientesDTO.add(dto);

            topNumber++;
        }

        Top3ResponseDTO listTopClientes = new Top3ResponseDTO(top3ClientesDTO);


        return listTopClientes;
    }
}