package com.example.DesafioSprint1.controller;


import com.example.DesafioSprint1.dto.Auth.LoginRequestDto;
import com.example.DesafioSprint1.dto.Auth.RegisterRequestDto;
import com.example.DesafioSprint1.service.AuthServiceImpl;
import com.example.DesafioSprint1.service.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    IAuthService service;

    public AuthController(AuthServiceImpl service){
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(service.login(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto){
        return ResponseEntity.ok(service.register(registerRequestDto));
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequestDto registerRequestDto){
        return ResponseEntity.ok(service.registerAdmin(registerRequestDto));
    }

}
