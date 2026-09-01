package br.com.alura.reserva_salas.exceptions;

public class UsuarioComReservaAtivaException extends RuntimeException {

    public UsuarioComReservaAtivaException(String message) {
        super(message);
    }
}
