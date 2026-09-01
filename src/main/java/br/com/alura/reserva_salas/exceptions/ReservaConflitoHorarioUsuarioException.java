package br.com.alura.reserva_salas.exceptions;

public class ReservaConflitoHorarioUsuarioException extends RuntimeException {
    public ReservaConflitoHorarioUsuarioException(String message) {
        super(message);
    }
}
