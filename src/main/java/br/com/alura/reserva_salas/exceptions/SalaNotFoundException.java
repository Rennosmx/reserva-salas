package br.com.alura.reserva_salas.exceptions;

public class SalaNotFoundException extends RuntimeException {
    public SalaNotFoundException(String message) {
        super(message);
    }
}
