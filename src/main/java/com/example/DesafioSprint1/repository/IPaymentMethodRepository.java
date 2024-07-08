package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Flight;
import com.example.DesafioSprint1.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

}
