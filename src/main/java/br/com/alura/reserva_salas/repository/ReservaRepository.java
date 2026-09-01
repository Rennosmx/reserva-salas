package br.com.alura.reserva_salas.repository;

import br.com.alura.reserva_salas.enums.ReservaStatus;
import br.com.alura.reserva_salas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
    SELECT COUNT(r) > 0
    FROM Reserva r
    WHERE (:reservaId IS NULL OR r.id <> :reservaId)
      AND r.sala.id = :salaId
      AND r.status = :status
      AND r.dataInicio < :dataFim
      AND r.dataFim > :dataInicio
""")
    boolean existeConflitoNaSala(
            Long reservaId,
            Long salaId,
            ReservaStatus status,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

    @Query("""
    SELECT COUNT(r) > 0
    FROM Reserva r
    WHERE (:reservaId IS NULL OR r.id <> :reservaId)
      AND r.usuario.id = :usuarioId
      AND r.status = :status
      AND r.dataInicio < :dataFim
      AND r.dataFim > :dataInicio
""")
    boolean existeConflitoComUsuario(
            Long reservaId,
            Long usuarioId,
            ReservaStatus status,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

    @Query("""
    SELECT r
    FROM Reserva r
    WHERE r.sala.id = :salaId
      AND r.dataInicio < :dataFim
      AND r.dataFim > :dataInicio
""")
    Page<Reserva> buscarPorSalaEPeriodo(
            Long salaId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    );

    boolean existsBySalaIdAndStatus(Long id, ReservaStatus reservaStatus);

    boolean existsByUsuarioIdAndStatus(Long id, ReservaStatus reservaStatus);
}