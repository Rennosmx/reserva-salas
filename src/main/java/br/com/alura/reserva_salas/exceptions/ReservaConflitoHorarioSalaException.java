package br.com.alura.reserva_salas.exceptions;

public class ReservaConflitoHorarioSalaException extends RuntimeException {
    public ReservaConflitoHorarioSalaException(String message) {
        super(message);
    }
}
