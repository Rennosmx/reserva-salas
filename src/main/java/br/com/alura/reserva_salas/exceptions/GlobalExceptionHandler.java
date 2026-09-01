package br.com.alura.reserva_salas.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SalaNotFoundException.class)
    public ResponseEntity<String> handleSalaNotFoundException(SalaNotFoundException e) {
        return ResponseEntity
                .notFound()
                .build();
    }

    @ExceptionHandler(SalaComReservaAtivaException.class)
    public ResponseEntity<String> handleSalaComReservaAtivaException(SalaComReservaAtivaException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> handleUsuarioNotFoundException(UsuarioNotFoundException e) {
        return ResponseEntity
                .notFound()
                .build();
    }

    @ExceptionHandler(UsuarioComReservaAtivaException.class)
    public ResponseEntity<String> handleUsuarioComReservaAtivaException(UsuarioComReservaAtivaException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ReservaHorarioInvalidoException.class)
    public ResponseEntity<String> handleReservaHorarioInvalidoException(ReservaHorarioInvalidoException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ReservaConflitoHorarioSalaException.class)
    public ResponseEntity<String> handleReservaConflitoHorarioSalaException(ReservaConflitoHorarioSalaException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ReservaConflitoHorarioUsuarioException.class)
    public ResponseEntity<String> handleReservaConflitoHorarioUsuarioException(ReservaConflitoHorarioUsuarioException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ReservaNotFoundException.class)
    public ResponseEntity<String> handleReservaNotFoundException(ReservaNotFoundException e) {
        return ResponseEntity
                .notFound()
                .build();
    }

    @ExceptionHandler(ReservaAlterarException.class)
    public ResponseEntity<String> handleReservaCancelarException(ReservaAlterarException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(SalaCapacidadeException.class)
    public ResponseEntity<String> handleSalaCapacidadeException(SalaCapacidadeException e) {
        return  ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

}
