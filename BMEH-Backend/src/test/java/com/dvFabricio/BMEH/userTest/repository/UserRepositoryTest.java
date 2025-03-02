//package com.dvFabricio.BMEH.userTest.repository;
//
//import com.dvFabricio.BMEH.domain.endereco.Endereco;
//import com.dvFabricio.BMEH.domain.endereco.Estado;
//import com.dvFabricio.BMEH.domain.user.User;
//import com.dvFabricio.BMEH.repositories.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private User user;
//
//    @BeforeEach
//    void setup() {
//        user = new User(
//                "testUser",
//                "test@example.com",
//                "password123",
//                "12345678901",
//                "11999999999",
//                new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000")
//        );
//        userRepository.save(user);
//    }
//
//    @Test
//    void findByEmail_ShouldReturnUser_WhenEmailExists() {
//        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
//
//        assertAll(
//                () -> assertTrue(foundUser.isPresent(), "O usuário deve ser encontrado"),
//                () -> assertEquals("testUser", foundUser.get().getLogin(), "O login do usuário deve ser 'testUser'"),
//                () -> assertEquals("test@example.com", foundUser.get().getEmail(), "O email do usuário deve ser 'test@example.com'"),
//                () -> assertEquals("12345678901", foundUser.get().getCpf(), "O CPF deve ser '12345678901'"),
//                () -> assertEquals("11999999999", foundUser.get().getTelefone(), "O telefone deve ser '11999999999'"),
//                () -> assertNotNull(foundUser.get().getEndereco(), "O endereço não deve ser nulo"),
//                () -> assertEquals("Rua A", foundUser.get().getEndereco().getRua(), "A rua deve ser 'Rua A'")
//        );
//    }
//
//    @Test
//    void findByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
//        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");
//
//        assertFalse(foundUser.isPresent(), "Nenhum usuário deve ser encontrado");
//    }
//
//    @Test
//    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
//        boolean exists = userRepository.existsByEmail("test@example.com");
//
//        assertTrue(exists, "O método deve retornar true quando o email existir");
//    }
//
//    @Test
//    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
//        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
//
//        assertFalse(exists, "O método deve retornar false quando o email não existir");
//    }
//
//
//
//    @Test
//    void existsByCpf_ShouldReturnTrue_WhenCpfExists() {
//        boolean exists = userRepository.existsByCpf("12345678901");
//
//        assertTrue(exists, "O método deve retornar true quando o CPF existir");
//    }
//
//    @Test
//    void existsByCpf_ShouldReturnFalse_WhenCpfDoesNotExist() {
//        boolean exists = userRepository.existsByCpf("98765432100");
//
//        assertFalse(exists, "O método deve retornar false quando o CPF não existir");
//    }
//}