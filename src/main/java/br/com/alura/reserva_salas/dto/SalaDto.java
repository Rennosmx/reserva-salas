package br.com.alura.reserva_salas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SalaDto(String nome, Integer capacidade) {
}
