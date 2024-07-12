package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {


}
