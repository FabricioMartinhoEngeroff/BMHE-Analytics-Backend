package com.dvFabricio.BMEH.controllers;

import com.dvFabricio.BMEH.domain.DTOs.UserRequestDTO;
import com.dvFabricio.BMEH.infra.exception.authorization.JwtException;
import com.dvFabricio.BMEH.infra.exception.database.MissingRequiredFieldException;
import com.dvFabricio.BMEH.infra.exception.resource.DuplicateResourceException;
import com.dvFabricio.BMEH.infra.exception.resource.ResourceNotFoundExceptions;
import com.dvFabricio.BMEH.infra.security.TokenService;
import com.dvFabricio.BMEH.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;


    @GetMapping("/me")
    public ResponseEntity<?>findAuthenticatedUser(@RequestHeader("Authorization") String token) {
        try {
            UUID userId = tokenService.getUserIdFromToken(token);
            return ResponseEntity.ok(userService.findUserById(userId));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (ResourceNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequestDTO));
        } catch (DuplicateResourceException e) {
            Map<String, String> errorResponse = Map.of(
                    "field", e.getField(),
                    "message", e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (MissingRequiredFieldException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.ok(userService.updateUser(uuid, userRequestDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ID format");
        } catch (ResourceNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            userService.deleteUser(uuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ID format");
        } catch (ResourceNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
