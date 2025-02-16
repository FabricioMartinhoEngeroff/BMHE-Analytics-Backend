package com.dvFabricio.BMEH.userTest.service;

import com.dvFabricio.BMEH.domain.DTOs.UserDTO;
import com.dvFabricio.BMEH.domain.DTOs.UserRequestDTO;
import com.dvFabricio.BMEH.domain.endereco.Endereco;
import com.dvFabricio.BMEH.domain.endereco.Estado;
import com.dvFabricio.BMEH.domain.user.User;
import com.dvFabricio.BMEH.infra.exception.resource.DuplicateResourceException;
import com.dvFabricio.BMEH.infra.exception.resource.ResourceNotFoundExceptions;
import com.dvFabricio.BMEH.repositories.UserRepository;
import com.dvFabricio.BMEH.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserRequestDTO userRequestDTODuplicado;

    @BeforeEach
    void setup() {
        user = new User("user", "user@example.com", "password", "12345678901", "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000"));

        userRequestDTO = new UserRequestDTO("user", "user@example.com", "password", "12345678901", "11999999999",
                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000"));

        userRequestDTODuplicado = new UserRequestDTO("user2", "user2@example.com", "password", "12345678901", "11999999999",
                new Endereco("Rua B", "456", "Bairro C", "Cidade D", Estado.RJ, "22041001"));
    }

    @Test
    void deveriaBuscarTodosOsUsuarios() {
        User user1 = new User("user1", "user1@example.com", "password1", "98765432100", "11888888888",
                new Endereco("Rua X", "456", "Bairro Y", "Cidade Z", Estado.RJ, "22041001"));

        User user2 = new User("user2", "user2@example.com", "password2", "12312312399", "11777777777",
                new Endereco("Rua Y", "789", "Bairro W", "Cidade K", Estado.MG, "30130000"));

        given(userRepository.findAll()).willReturn(List.of(user1, user2));

        List<UserDTO> result = userService.findAllUsers();

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("user1", result.get(0).login()),
                () -> assertEquals("user1@example.com", result.get(0).email()),
                () -> assertEquals("user2", result.get(1).login()),
                () -> assertEquals("user2@example.com", result.get(1).email())
        );

        then(userRepository).should().findAll();
    }

    @Test
    void deveriaCadastrarNovoUsuario() {
        given(userRepository.existsByEmail(userRequestDTO.email())).willReturn(false);
        given(userRepository.existsByCpf(userRequestDTO.cpf())).willReturn(false);
        given(passwordEncoder.encode(userRequestDTO.password())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(user);

        UserDTO result = userService.createUser(userRequestDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("user@example.com", result.email()),
                () -> assertEquals("user", result.login()),
                () -> assertEquals("12345678901", result.cpf()),
                () -> assertEquals("11999999999", result.telefone()),
                () -> assertNotNull(result.endereco()),
                () -> assertEquals("Rua A", result.endereco().getRua())
        );

        then(userRepository).should().existsByEmail(userRequestDTO.email());
        then(userRepository).should().existsByCpf(userRequestDTO.cpf());
        then(passwordEncoder).should().encode(userRequestDTO.password());
        then(userRepository).should().save(any(User.class));
    }

    @Test
    void naoDeveriaCadastrarUsuarioComCpfDuplicado() {
        given(userRepository.existsByEmail(userRequestDTODuplicado.email())).willReturn(false);
        given(userRepository.existsByCpf(userRequestDTODuplicado.cpf())).willReturn(true);

        DuplicateResourceException cpfException = assertThrows(
                DuplicateResourceException.class,
                () -> userService.createUser(userRequestDTODuplicado)
        );

        assertEquals("A user with this CPF already exists.", cpfException.getMessage());

        then(userRepository).should().existsByEmail(userRequestDTODuplicado.email());
        then(userRepository).should().existsByCpf(userRequestDTODuplicado.cpf());
        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    void deveriaAtualizarUsuario() {
        UUID userId = UUID.randomUUID();
        user.setId(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.encode("updatedPassword")).willReturn("encodedUpdatedPassword");
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0, User.class));

        UserRequestDTO updateRequest = new UserRequestDTO("updatedUser", "updated@example.com", "updatedPassword",
                "98765432100", "11988888888", new Endereco("Rua Nova", "999", "Centro", "Cidade Z", Estado.MG, "30130000"));

        UserDTO result = userService.updateUser(userId, updateRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("updatedUser", result.login()),
                () -> assertEquals("updated@example.com", result.email()),
                () -> assertEquals("98765432100", result.cpf()),
                () -> assertEquals("11988888888", result.telefone()),
                () -> assertNotNull(result.endereco()),
                () -> assertEquals("Rua Nova", result.endereco().getRua())
        );

        then(userRepository).should().findById(userId);
        then(passwordEncoder).should().encode("updatedPassword");
        then(userRepository).should().save(any(User.class));
    }

    @Test
    void deveriaDeletarUsuarioExistente() {
        UUID userId = user.getId();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(userId));

        then(userRepository).should().findById(userId);
        then(userRepository).should().delete(user);
    }

    @Test
    void naoDeveriaDeletarUsuarioNaoEncontrado() {
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        ResourceNotFoundExceptions exception = assertThrows(
                ResourceNotFoundExceptions.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals("User not found with id: " + userId, exception.getMessage());
        then(userRepository).should().findById(userId);
        then(userRepository).should(never()).delete(any(User.class));
    }
}

