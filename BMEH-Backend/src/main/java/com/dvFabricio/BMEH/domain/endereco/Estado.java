package com.dvFabricio.BMEH.domain.endereco;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Estado {
    ACRE("Acre"),
    ALAGOAS("Alagoas"),
    AMAPA("Amapá"),
    AMAZONAS("Amazonas"),
    BAHIA("Bahia"),
    CEARA("Ceará"),
    DISTRITO_FEDERAL("Distrito Federal"),
    ESPIRITO_SANTO("Espírito Santo"),
    GOIAS("Goiás"),
    MARANHAO("Maranhão"),
    MATO_GROSSO("Mato Grosso"),
    MATO_GROSSO_DO_SUL("Mato Grosso do Sul"),
    MINAS_GERAIS("Minas Gerais"),
    PARA("Pará"),
    PARAIBA("Paraíba"),
    PARANA("Paraná"),
    PERNAMBUCO("Pernambuco"),
    PIAUI("Piauí"),
    RIO_DE_JANEIRO("Rio de Janeiro"),
    RIO_GRANDE_DO_NORTE("Rio Grande do Norte"),
    RIO_GRANDE_DO_SUL("Rio Grande do Sul"),
    RONDONIA("Rondônia"),
    RORAIMA("Roraima"),
    SANTA_CATARINA("Santa Catarina"),
    SAO_PAULO("São Paulo"),
    SERGIPE("Sergipe"),
    TOCANTINS("Tocantins");

    private final String nome;

    Estado(String nome) {
        this.nome = nome;
    }

    @JsonValue
    public String getNome() {
        return nome;
    }

    @JsonCreator
    public static Estado fromNome(String nome) {
        String nomeTrim = nome.trim();

        return Arrays.stream(values())
                .filter(estado -> estado.nome.equalsIgnoreCase(nomeTrim))
                .findFirst()
                .orElseThrow(() -> {
                    System.out.println("❌ Estado inválido: " + nome);
                    System.out.println("📌 Estados disponíveis: " + Arrays.toString(Estado.values()));
                    return new IllegalArgumentException("Estado inválido. Use um dos seguintes: " + Arrays.toString(Estado.values()));
                });
    }
}
