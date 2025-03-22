package com.dvFabricio.BMEH.domain.endereco;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;

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
        String nomeNormalizado = Normalizer
                .normalize(nome.trim(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toUpperCase(Locale.ROOT)
                .replace(" ", "_");

        return Arrays.stream(values())
                .filter(estado -> estado.name().equalsIgnoreCase(nomeNormalizado)
                        || estado.nome.equalsIgnoreCase(nome.trim()))
                .findFirst()
                .orElseThrow(() -> {
                    System.err.println("❌ Estado inválido: " + nome);
                    System.err.println("📌 Estados disponíveis: " + Arrays.toString(Estado.values()));
                    return new IllegalArgumentException("Estado inválido. Use um dos seguintes: " + Arrays.toString(Estado.values()));
                });
    }
}
