package br.com.alura.reserva_salas.repository;

import br.com.alura.reserva_salas.enums.ReservaStatus;
import br.com.alura.reserva_salas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {



}
