package com.dvFabricio.BMEH.userTest.domain;


import com.dvFabricio.BMEH.domain.endereco.Endereco;
import com.dvFabricio.BMEH.domain.endereco.Estado;
import com.dvFabricio.BMEH.domain.user.Role;
import com.dvFabricio.BMEH.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    @Test
    void testUserCreation() {
        Endereco endereco = new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000");
        User user = new User("fabricio", "fabricio@example.com", "password123", "12345678901", "11999999999", endereco);

        assertNotNull(user);
        assertEquals("fabricio", user.getLogin());
        assertEquals("fabricio@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("12345678901", user.getCpf());
        assertEquals("11999999999", user.getTelefone());
        assertNotNull(user.getEndereco());
        assertEquals("Rua A", user.getEndereco().getRua());
        assertEquals("SP", user.getEndereco().getEstado().toString());
    }

    @Test
    void testUserRolesAssociation() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        Endereco endereco = new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000");
        User user = new User("fabricio", "fabricio@example.com", "password123", "12345678901", "11999999999", endereco);
        user.setRoles(List.of(adminRole, userRole));

        assertNotNull(user.getRoles());
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_USER")));
    }

    @Test
    void testUserDetailsImplementation() {
        Role userRole = new Role("ROLE_USER");

        Endereco endereco = new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000");
        User user = new User("fabricio", "fabricio@example.com", "password123", "12345678901", "11999999999", endereco);
        user.setRoles(List.of(userRole));

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
        assertEquals("fabricio", user.getUsername());
        assertEquals(1, user.getAuthorities().size());
        assertEquals("ROLE_USER", user.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testUserUuidGeneration() {
        Endereco endereco = new Endereco("Rua A", "123", "Bairro B", "Cidade C", Estado.SP, "01001000");
        User user = new User("fabricio", "fabricio@example.com", "password123", "12345678901", "11999999999", endereco);

        user.setId(UUID.randomUUID());

        assertNotNull(user.getId());
        assertInstanceOf(UUID.class, user.getId());
    }
}

