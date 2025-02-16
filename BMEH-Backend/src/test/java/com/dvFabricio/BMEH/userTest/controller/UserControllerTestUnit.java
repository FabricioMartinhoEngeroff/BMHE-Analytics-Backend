package com.dvFabricio.BMEH.userTest.controller;

import com.dvFabricio.BMEH.controllers.UserController;
import com.dvFabricio.BMEH.domain.DTOs.UserDTO;
import com.dvFabricio.BMEH.domain.DTOs.UserRequestDTO;
import com.dvFabricio.BMEH.domain.endereco.Endereco;
import com.dvFabricio.BMEH.domain.endereco.Estado;
import com.dvFabricio.BMEH.infra.exception.database.MissingRequiredFieldException;
import com.dvFabricio.BMEH.infra.exception.resource.DuplicateResourceException;
import com.dvFabricio.BMEH.infra.exception.resource.ResourceNotFoundExceptions;
import com.dvFabricio.BMEH.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class UserControllerTestUnit {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void findAllUsers_ShouldReturnListOfUsers() {
        List<UserDTO> userList = List.of(
                new UserDTO(UUID.randomUUID(), "login1", "email1@test.com", List.of("ROLE_USER"),
                        "12345678901", "11999999999", new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000"))
        );

        Mockito.when(userService.findAllUsers()).thenReturn(userList);

        ResponseEntity<List<UserDTO>> response = userController.findAllUsers();

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().equals(userList);
    }

    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidInput() {
        UserRequestDTO request = new UserRequestDTO(
                "login1", "email1@test.com", "password1", "12345678901", "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000")
        );

        UserDTO userDTO = new UserDTO(
                UUID.randomUUID(), "login1", "email1@test.com", List.of("ROLE_USER"),
                "12345678901", "11999999999", request.endereco()
        );

        Mockito.when(userService.createUser(request)).thenReturn(userDTO);

        ResponseEntity<?> response = userController.createUser(request);

        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody().equals(userDTO);
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenDuplicateEmail() {
        UserRequestDTO request = new UserRequestDTO(
                "login1", "email1@test.com", "password1", "12345678901", "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000")
        );

        Mockito.when(userService.createUser(request))
                .thenThrow(new DuplicateResourceException("email", "A user with this email already exists."));

        ResponseEntity<?> response = userController.createUser(request);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assert responseBody != null;
        assert responseBody.get("field").equals("email");
        assert responseBody.get("message").equals("A user with this email already exists.");
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenValidInput() {
        UUID userId = UUID.randomUUID();
        UserRequestDTO request = new UserRequestDTO(
                "login2", "email2@test.com", "password2", "98765432100", "11988888888",
                new Endereco("Rua Nova", "999", "Centro", "Cidade Z", Estado.MG, "30130000")
        );

        UserDTO userDTO = new UserDTO(
                userId, "login2", "email2@test.com", List.of("ROLE_USER"),
                "98765432100", "11988888888", request.endereco()
        );

        Mockito.when(userService.updateUser(userId, request)).thenReturn(userDTO);

        ResponseEntity<?> response = userController.updateUser(userId.toString(), request);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().equals(userDTO);
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserDeleted() {
        UUID userId = UUID.randomUUID();

        ResponseEntity<?> response = userController.deleteUser(userId.toString());

        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
        Mockito.verify(userService).deleteUser(userId);
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        Mockito.doThrow(new ResourceNotFoundExceptions("User not found")).when(userService).deleteUser(userId);

        ResponseEntity<?> response = userController.deleteUser(userId.toString());

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getBody().equals("User not found");
    }
}