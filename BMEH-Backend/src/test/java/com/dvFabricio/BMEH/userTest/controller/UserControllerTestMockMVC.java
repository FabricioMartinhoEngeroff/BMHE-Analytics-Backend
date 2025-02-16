package com.dvFabricio.BMEH.userTest.controller;

import com.dvFabricio.BMEH.controllers.UserController;
import com.dvFabricio.BMEH.domain.DTOs.UserDTO;
import com.dvFabricio.BMEH.domain.DTOs.UserRequestDTO;
import com.dvFabricio.BMEH.domain.endereco.Endereco;
import com.dvFabricio.BMEH.domain.endereco.Estado;
import com.dvFabricio.BMEH.infra.exception.resource.DuplicateResourceException;
import com.dvFabricio.BMEH.infra.exception.resource.ResourceNotFoundExceptions;
import com.dvFabricio.BMEH.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTestMockMVC {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    void findAllUsers_ShouldReturnListOfUsers() throws Exception {
        List<UserDTO> userList = List.of(
                new UserDTO(
                        UUID.randomUUID(),
                        "login1",
                        "email1@test.com",
                        List.of("ROLE_USER"),
                        "12345678901",
                        "11999999999",
                        new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000")
                )
        );

        doReturn(userList).when(userService).findAllUsers();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].login").value("login1"))
                .andExpect(jsonPath("$[0].email").value("email1@test.com"))
                .andExpect(jsonPath("$[0].cpf").value("12345678901"))
                .andExpect(jsonPath("$[0].telefone").value("11999999999"))
                .andExpect(jsonPath("$[0].endereco.rua").value("Rua A"));

        verify(userService).findAllUsers();
    }

    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidInput() throws Exception {
        UserRequestDTO request = new UserRequestDTO(
                "login1", "email1@test.com", "password1", "12345678901", "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000")
        );

        UserDTO userDTO = new UserDTO(
                UUID.randomUUID(), "login1", "email1@test.com", List.of("ROLE_USER"),
                "12345678901", "11999999999", request.endereco()
        );

        doReturn(userDTO).when(userService).createUser(argThat(req ->
                req.login().equals("login1") &&
                        req.email().equals("email1@test.com") &&
                        req.password().equals("password1") &&
                        req.cpf().equals("12345678901") &&
                        req.telefone().equals("11999999999")
        ));

        String requestBody = """
                {
                    "login": "login1",
                    "email": "email1@test.com",
                    "password": "password1",
                    "cpf": "12345678901",
                    "telefone": "11999999999",
                    "endereco": {
                        "rua": "Rua A",
                        "numero": "123",
                        "bairro": "Bairro B",
                        "cidade": "Cidade C",
                        "estado": "SP",
                        "cep": "01001000"
                    }
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("login1"))
                .andExpect(jsonPath("$.email").value("email1@test.com"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.telefone").value("11999999999"))
                .andExpect(jsonPath("$.endereco.rua").value("Rua A"));

        verify(userService).createUser(any(UserRequestDTO.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenValidInput() throws Exception {
        UUID userId = UUID.randomUUID();
        UserRequestDTO request = new UserRequestDTO(
                "login2", "email2@test.com", "password2", "98765432100", "11988888888",
                new Endereco("Rua Nova", "999", "Centro", "Cidade Z", Estado.MG, "30130000")
        );

        UserDTO userDTO = new UserDTO(
                userId, "login2", "email2@test.com", List.of("ROLE_USER"),
                "98765432100", "11988888888", request.endereco()
        );

        doReturn(userDTO).when(userService).updateUser(eq(userId), argThat(req ->
                req.login().equals("login2") &&
                        req.email().equals("email2@test.com") &&
                        req.password().equals("password2") &&
                        req.cpf().equals("98765432100") &&
                        req.telefone().equals("11988888888")
        ));

        String requestBody = """
                {
                    "login": "login2",
                    "email": "email2@test.com",
                    "password": "password2",
                    "cpf": "98765432100",
                    "telefone": "11988888888",
                    "endereco": {
                        "rua": "Rua Nova",
                        "numero": "999",
                        "bairro": "Centro",
                        "cidade": "Cidade Z",
                        "estado": "MG",
                        "cep": "30130000"
                    }
                }
                """;

        mockMvc.perform(put("/users/{id}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("login2"))
                .andExpect(jsonPath("$.email").value("email2@test.com"))
                .andExpect(jsonPath("$.cpf").value("98765432100"))
                .andExpect(jsonPath("$.telefone").value("11988888888"))
                .andExpect(jsonPath("$.endereco.rua").value("Rua Nova"));

        verify(userService).updateUser(eq(userId), any(UserRequestDTO.class));
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserDeleted() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId.toString()))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        UUID userId = UUID.randomUUID();

        doThrow(new ResourceNotFoundExceptions("User not found with id: " + userId))
                .when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: " + userId));

        verify(userService).deleteUser(userId);
    }
}

