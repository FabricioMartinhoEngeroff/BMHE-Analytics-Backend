package com.dvFabricio.BMEH.userTest.controller;

import com.dvFabricio.BMEH.controllers.AuthController;
import com.dvFabricio.BMEH.domain.DTOs.LoginRequestDTO;
import com.dvFabricio.BMEH.domain.DTOs.LoginResponseDTO;
import com.dvFabricio.BMEH.domain.DTOs.RegisterRequestDTO;
import com.dvFabricio.BMEH.domain.endereco.Endereco;
import com.dvFabricio.BMEH.domain.endereco.Estado;
import com.dvFabricio.BMEH.domain.user.Role;
import com.dvFabricio.BMEH.domain.user.User;
import com.dvFabricio.BMEH.infra.security.TokenService;
import com.dvFabricio.BMEH.repositories.RoleRepository;
import com.dvFabricio.BMEH.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(
                "userLogin",
                "user@example.com",
                "encodedPassword123",
                "12345678901",
                "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000")
        );
        user.setId(UUID.randomUUID());
    }

    @Test
    void login_ShouldReturnToken_WhenValidCredentials() {
        String email = "user@example.com";
        String password = "password123";
        String token = "mockToken";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        Mockito.when(tokenService.generateToken(user)).thenReturn(token);

        LoginRequestDTO request = new LoginRequestDTO(email, password);
        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponseDTO responseBody = (LoginResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals("userLogin", responseBody.login());
        assertEquals(token, responseBody.token());
    }

    @Test
    void login_ShouldReturnBadRequest_WhenEmailIsEmpty() {
        LoginRequestDTO request = new LoginRequestDTO("", "password123");

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email cannot be empty.", response.getBody());
    }

    @Test
    void login_ShouldReturnNotFound_WhenUserDoesNotExist() {
        String email = "user@example.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        LoginRequestDTO request = new LoginRequestDTO(email, "password123");
        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenPasswordIsInvalid() {
        String email = "user@example.com";
        String password = "password123";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        LoginRequestDTO request = new LoginRequestDTO(email, password);
        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void register_ShouldCreateUser_WhenValidInput() {
        String email = "newuser@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";
        String login = "newUser";
        String token = "mockToken";

        Role role = new Role("ROLE_USER");
        role.setId(UUID.randomUUID());

        User newUser = new User(
                login,
                email,
                encodedPassword,
                "98765432100",
                "11988888888",
                new Endereco("Rua Nova", "999", "Centro", "Cidade Z", Estado.MG, "30130000")
        );
        newUser.setId(UUID.randomUUID());

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(userRepository.existsByCpf("98765432100")).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });
        Mockito.when(tokenService.generateToken(Mockito.any(User.class))).thenReturn(token);

        RegisterRequestDTO request = new RegisterRequestDTO(login, email, password, "98765432100", "11988888888",
                new Endereco("Rua Nova", "999", "Centro", "Cidade Z", Estado.MG, "30130000"));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LoginResponseDTO responseBody = (LoginResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(login, responseBody.login());
        assertEquals(token, responseBody.token());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenEmailAlreadyExists() {
        String email = "existinguser@example.com";
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);

        RegisterRequestDTO request = new RegisterRequestDTO("login", email, "password123", "12345678901", "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000"));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email is already in use.", response.getBody());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenCpfAlreadyExists() {
        String cpf = "12345678901";
        Mockito.when(userRepository.existsByCpf(cpf)).thenReturn(true);

        RegisterRequestDTO request = new RegisterRequestDTO("login", "user@example.com", "password123", cpf, "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000"));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CPF is already in use.", response.getBody());
    }

    @Test
    void register_ShouldReturnInternalServerError_WhenRoleNotFound() {
        String email = "newuser@example.com";
        String password = "password123";
        String login = "newUser";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(userRepository.existsByCpf("98765432100")).thenReturn(false);
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        RegisterRequestDTO request = new RegisterRequestDTO(login, email, password, "98765432100", "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000"));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Role 'ROLE_USER' not found", response.getBody());
    }
}
