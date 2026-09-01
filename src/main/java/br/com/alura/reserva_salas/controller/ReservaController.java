package br.com.alura.reserva_salas.controller;

import br.com.alura.reserva_salas.dto.ReservaDto;
import br.com.alura.reserva_salas.service.ReservaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaDto> criarReserva(@RequestBody ReservaDto reservaDto) {
        return ResponseEntity.ok(reservaService.criar(reservaDto));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<ReservaDto> concluirReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.concluirReserva(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDto> atualizarReserva(@RequestBody ReservaDto reservaDto, @PathVariable Long id) {
        return ResponseEntity.ok(reservaService.atualizar(reservaDto, id));
    }

    @GetMapping
    public ResponseEntity<List<ReservaDto>> listar() {
        return ResponseEntity.ok(reservaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> buscarReservaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<Page<ReservaDto>> listarPorSalaEPeriodo(
            @PathVariable Long salaId,
            @RequestParam LocalDateTime dataInicio,
            @RequestParam LocalDateTime dataFim,
            Pageable pageable) {

        return ResponseEntity.ok(reservaService.listarPorSalaEPeriodo(salaId, dataInicio, dataFim, pageable));
    }

}
