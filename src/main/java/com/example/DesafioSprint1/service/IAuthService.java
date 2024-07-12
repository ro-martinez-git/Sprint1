package com.example.DesafioSprint1.service;


import com.example.DesafioSprint1.dto.Auth.AuthResponseDto;
import com.example.DesafioSprint1.dto.Auth.LoginRequestDto;
import com.example.DesafioSprint1.dto.Auth.RegisterRequestDto;

public interface IAuthService {
    AuthResponseDto login(LoginRequestDto userDto);
    AuthResponseDto register(RegisterRequestDto userToRegisterDto);
    AuthResponseDto registerAdmin(RegisterRequestDto userToRegisterDto);
}
