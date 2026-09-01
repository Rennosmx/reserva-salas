package br.com.alura.reserva_salas.service;

import br.com.alura.reserva_salas.dto.ReservaDto;
import br.com.alura.reserva_salas.enums.ReservaStatus;
import br.com.alura.reserva_salas.exceptions.*;
import br.com.alura.reserva_salas.model.Reserva;
import br.com.alura.reserva_salas.model.Sala;
import br.com.alura.reserva_salas.model.Usuario;
import br.com.alura.reserva_salas.repository.ReservaRepository;
import br.com.alura.reserva_salas.repository.SalaRepository;
import br.com.alura.reserva_salas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;

    public ReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository, SalaRepository salaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
    }

    @Transactional
    public ReservaDto criar(ReservaDto reservaDto) {

        if (!validarHorarioReservaValido(reservaDto)) {
            throw new ReservaHorarioInvalidoException("A data de início da reserva deve ser anterior à data de término.");
        }

        if (!validarSala(reservaDto, null)) {
            throw new ReservaConflitoHorarioSalaException("Sala já possui uma reserva ativa no horário informado.");
        }

        if (!validarUsuario(reservaDto, null)) {
            throw new ReservaConflitoHorarioUsuarioException("Usuário já possui uma reserva ativa no horário informado.");
        }

        var usuario = usuarioRepository.findById(reservaDto.usuario())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado."));

        var sala = salaRepository.findById(reservaDto.sala())
                .orElseThrow(() -> new SalaNotFoundException("Sala não encontrada."));

        var reserva = new Reserva(reservaDto.dataInicio(), reservaDto.dataFim(), usuario, sala);

        reservaRepository.save(reserva);

        return new ReservaDto(reserva.getDataInicio(), reserva.getDataFim(), reserva.getStatus(), usuario.getId(), sala.getId()
        );
    }

    @Transactional
    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNotFoundException("Reserva não encontrada."));

        validarReservaAtiva(reserva);

        reserva.setStatus(ReservaStatus.CANCELADA);
        reservaRepository.save(reserva);
    }

    @Transactional
    public ReservaDto concluirReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNotFoundException("Reserva não encontrada."));

        validarReservaAtiva(reserva);

        reserva.setStatus(ReservaStatus.CONCLUIDA);
        reservaRepository.save(reserva);

        return new ReservaDto(reserva.getDataInicio(), reserva.getDataFim(), reserva.getStatus(), reserva.getUsuario().getId(), reserva.getSala().getId());
    }

    @Transactional
    public ReservaDto atualizar(ReservaDto reservaDto, Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNotFoundException("Reserva não encontrada."));

        validarReservaAtiva(reserva);

        if (!validarHorarioReservaValido(reservaDto)) {
            throw new ReservaHorarioInvalidoException("A data de início da reserva deve ser anterior à data de término.");
        }

        Usuario usuario = usuarioRepository.findById(reservaDto.usuario())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado."));

        Sala sala = salaRepository.findById(reservaDto.sala())
                .orElseThrow(() -> new SalaNotFoundException("Sala não encontrada."));

        if (!validarSala(reservaDto, id)) {
            throw new ReservaConflitoHorarioSalaException("Sala já possui uma reserva ativa no horário informado.");
        }

        if (!validarUsuario(reservaDto, id)) {
            throw new ReservaConflitoHorarioUsuarioException("Usuário já possui uma reserva ativa no horário informado.");
        }

        reserva.setDataInicio(reservaDto.dataInicio());
        reserva.setDataFim(reservaDto.dataFim());
        reserva.setUsuario(usuario);
        reserva.setSala(sala);

        reservaRepository.save(reserva);

        return new ReservaDto(reserva.getDataInicio(), reserva.getDataFim(), reserva.getStatus(), reserva.getUsuario().getId(), reserva.getSala().getId());
    }

    public ReservaDto buscarPorId(Long id) {
        var reserva = reservaRepository.findById(id).orElseThrow(() -> new ReservaNotFoundException("Reserva não encontrada."));
        return converterParaDto(reserva);
    }

    public List<ReservaDto> listar() {
        return reservaRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    public Page<ReservaDto> listarPorSalaEPeriodo(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {

        salaRepository.findById(salaId).orElseThrow(() -> new SalaNotFoundException("Sala não encontrada."));

        if (!dataInicio.isBefore(dataFim)) {
            throw new ReservaHorarioInvalidoException("A data de início deve ser anterior à data de término.");
        }

        return reservaRepository.buscarPorSalaEPeriodo(salaId, dataInicio,dataFim, pageable).map(this::converterParaDto);
    }

    private void validarReservaAtiva(Reserva reserva) {
        if (reserva.getStatus() != ReservaStatus.ATIVA) {
            throw new ReservaAlterarException(
                    "A reserva deve estar com status ATIVA para poder ser alterada."
            );
        }
    }

    private boolean validarHorarioReservaValido(ReservaDto reservaDto) {
        return reservaDto.dataInicio().isBefore(reservaDto.dataFim());
    }

    private boolean validarSala(ReservaDto reservaDto, Long id) {
        return !reservaRepository.existeConflitoNaSala(
                id,
                reservaDto.sala(),
                ReservaStatus.ATIVA,
                reservaDto.dataInicio(),
                reservaDto.dataFim()
        );
    }

    private boolean validarUsuario(ReservaDto reservaDto, Long id) {
        return !reservaRepository.existeConflitoComUsuario(
                id,
                reservaDto.usuario(),
                ReservaStatus.ATIVA,
                reservaDto.dataInicio(),
                reservaDto.dataFim()
        );
    }

    private ReservaDto converterParaDto(Reserva reserva) {
        return new ReservaDto(
                reserva.getDataInicio(),
                reserva.getDataFim(),
                reserva.getStatus(),
                reserva.getUsuario().getId(),
                reserva.getSala().getId()
        );
    }

}
