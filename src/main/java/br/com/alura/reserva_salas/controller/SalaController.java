package br.com.alura.reserva_salas.controller;

import br.com.alura.reserva_salas.dto.SalaDto;
import br.com.alura.reserva_salas.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @PostMapping
    public ResponseEntity<SalaDto> criarSala(@RequestBody SalaDto salaDto) {
        return ResponseEntity.ok(salaService.criar(salaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSala(@PathVariable Long id) {
        salaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaDto> atualizarSala(@RequestBody SalaDto salaDto, @PathVariable Long id) {
        return ResponseEntity.ok(salaService.atualizar(salaDto, id));
    }

    @GetMapping
    public ResponseEntity<List<SalaDto>> listar() {
        return ResponseEntity.ok(salaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaDto> buscarSalaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.buscarPorId(id));
    }

}
