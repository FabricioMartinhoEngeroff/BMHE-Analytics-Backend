package com.dvFabricio.BMEH.controllers;

import com.dvFabricio.BMEH.domain.DTOs.LoginRequestDTO;
import com.dvFabricio.BMEH.domain.DTOs.LoginResponseDTO;
import com.dvFabricio.BMEH.domain.DTOs.RegisterRequestDTO;
import com.dvFabricio.BMEH.domain.user.Role;
import com.dvFabricio.BMEH.domain.user.User;
import com.dvFabricio.BMEH.infra.exception.resource.ResourceNotFoundExceptions;
import com.dvFabricio.BMEH.infra.security.TokenService;
import com.dvFabricio.BMEH.repositories.RoleRepository;
import com.dvFabricio.BMEH.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO body) {
        if (body.email() == null || body.email().isBlank()) {
            return ResponseEntity.badRequest().body("Email cannot be empty.");
        }

        try {
            User user = repository.findByEmail(body.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!passwordEncoder.matches(body.password(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(
                    user.getName(),
                    user.getEmail(),
                    user.getCpf(),
                    user.getTelefone(),
                    user.getEndereco(),
                    token
            ));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO body) {
        System.out.println("➡️ Recebendo dados do frontend: " + body);

        if (repository.existsByEmail(body.email())) {
            return ResponseEntity.badRequest().body("Email is already in use.");
        }
        if (repository.existsByCpf(body.cpf())) {
            return ResponseEntity.badRequest().body("CPF is already in use.");
        }

        try {
            User newUser = new User(
                    body.name(),
                    body.email(),
                    passwordEncoder.encode(body.password()),
                    body.cpf(),
                    body.telefone(),
                    body.endereco()
            );

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new ResourceNotFoundExceptions("Role 'ROLE_USER' not found"));

            newUser.setRoles(List.of(userRole));
            repository.save(newUser);

            String token = tokenService.generateToken(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponseDTO(
                    newUser.getName(),
                    newUser.getEmail(),
                    newUser.getCpf(),
                    newUser.getTelefone(),
                    newUser.getEndereco(),
                    token
            ));
        } catch (ResourceNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
