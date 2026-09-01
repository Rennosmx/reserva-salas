package br.com.alura.reserva_salas.dto;

import br.com.alura.reserva_salas.enums.ReservaStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
@JsonIgnoreProperties(ignoreUnknown = true)
public record ReservaDto(LocalDateTime dataInicio, LocalDateTime dataFim, ReservaStatus status, Long usuario, Long sala) {
}
