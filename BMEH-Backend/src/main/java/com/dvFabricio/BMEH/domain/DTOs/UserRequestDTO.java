package com.dvFabricio.BMEH.domain.DTOs;


import com.dvFabricio.BMEH.domain.endereco.Endereco;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Login não pode estar vazio")
        String login,

        @NotBlank(message = "Email não pode estar vazio")
        @Email(message = "Email deve ser válido")
        String email,

        @NotBlank(message = "Senha não pode estar vazia")
        @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
        String password,

        @NotBlank(message = "CPF não pode estar vazio")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
        String cpf,

        @NotBlank(message = "Telefone não pode estar vazio")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter entre 10 e 11 dígitos")
        String telefone,

        Endereco endereco
) {
}
