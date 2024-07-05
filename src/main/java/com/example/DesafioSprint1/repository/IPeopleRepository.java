package com.example.DesafioSprint1.repository;

import com.example.DesafioSprint1.model.Hotel;
import com.example.DesafioSprint1.model.People;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPeopleRepository extends JpaRepository<People, Long> {


}
