package br.com.alura.reserva_salas.service;

import br.com.alura.reserva_salas.dto.SalaDto;
import br.com.alura.reserva_salas.enums.ReservaStatus;
import br.com.alura.reserva_salas.exceptions.SalaCapacidadeException;
import br.com.alura.reserva_salas.exceptions.SalaComReservaAtivaException;
import br.com.alura.reserva_salas.exceptions.SalaNotFoundException;
import br.com.alura.reserva_salas.model.Sala;
import br.com.alura.reserva_salas.repository.ReservaRepository;
import br.com.alura.reserva_salas.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalaService {

    private final SalaRepository salaRepository;
    private final ReservaRepository reservaRepository;

    public SalaService(SalaRepository salaRepository, ReservaRepository reservaRepository) {
        this.salaRepository = salaRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional
    public SalaDto criar(SalaDto salaDto) {
        validarCapacidade(salaDto.capacidade());
        var sala = new Sala(salaDto.nome(), salaDto.capacidade());
        salaRepository.save(sala);
        return new SalaDto(sala.getNome(), sala.getCapacidade());
    }

    @Transactional
    public void deletar(Long id) {

        var sala = salaRepository.findById(id)
                .orElseThrow(() -> new SalaNotFoundException("Sala não encontrada"));

        boolean possuiReservaAtiva =
                reservaRepository.existsBySalaIdAndStatus(id, ReservaStatus.ATIVA);

        if (possuiReservaAtiva) {
            throw new SalaComReservaAtivaException(
                    "Não é possível deletar a sala, pois existem reservas ativas associadas a ela."
            );
        }
        salaRepository.delete(sala);
    }

    @Transactional
    public SalaDto atualizar(SalaDto salaDto, Long id) {

        var sala = salaRepository.findById(id)
                .orElseThrow(() -> new SalaNotFoundException("Sala não encontrada"));

        boolean possuiReservaAtiva =
                reservaRepository.existsBySalaIdAndStatus(id, ReservaStatus.ATIVA);

        if (possuiReservaAtiva) {
            throw new SalaComReservaAtivaException(
                    "Não é possível atualizar a sala, pois existem reservas ativas associadas a ela."
            );
        }

        if(salaDto.nome() != null){
            sala.setNome(salaDto.nome());
        }
        if (salaDto.capacidade() != null) {
            validarCapacidade(salaDto.capacidade());
            sala.setCapacidade(salaDto.capacidade());
        }

        salaRepository.save(sala);

        return new SalaDto(sala.getNome(), sala.getCapacidade());
    }

    public List<SalaDto> listar() {
        return salaRepository.findAll().stream()
                .map(sala -> new SalaDto(sala.getNome(), sala.getCapacidade()))
                .toList();
    }

    public SalaDto buscarPorId(Long id) {
        var sala = salaRepository.findById(id)
                .orElseThrow(() -> new SalaNotFoundException("Sala não encontrada"));

        return new SalaDto(
                sala.getNome(),
                sala.getCapacidade()
        );
    }

    private void validarCapacidade(Integer capacidade) {
        if (capacidade == null || capacidade <= 0) {
            throw new SalaCapacidadeException("A capacidade da sala deve ser maior que zero.");
        }
    }

}
