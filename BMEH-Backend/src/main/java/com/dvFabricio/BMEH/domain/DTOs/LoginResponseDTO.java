package com.dvFabricio.BMEH.domain.DTOs;

import com.dvFabricio.BMEH.domain.endereco.Endereco;

public record LoginResponseDTO(
        String name,
        String email,
        String cpf,
        String telefone,
        Endereco endereco,
        String token
) {}