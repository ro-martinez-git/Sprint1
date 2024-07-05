package com.example.DesafioSprint1.repository;


import com.example.DesafioSprint1.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<Cliente,Long> {

    Optional<Cliente> findByUsername(String username);
}
