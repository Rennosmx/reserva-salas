package br.com.alura.reserva_salas.service;

import br.com.alura.reserva_salas.dto.UsuarioDto;
import br.com.alura.reserva_salas.enums.ReservaStatus;
import br.com.alura.reserva_salas.exceptions.UsuarioComReservaAtivaException;
import br.com.alura.reserva_salas.exceptions.UsuarioNotFoundException;
import br.com.alura.reserva_salas.model.Usuario;
import br.com.alura.reserva_salas.repository.ReservaRepository;
import br.com.alura.reserva_salas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            ReservaRepository reservaRepository) {

        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional
    public UsuarioDto criar(UsuarioDto usuarioDto) {
        var usuario = new Usuario(usuarioDto.nome());
        usuarioRepository.save(usuario);
        return new UsuarioDto(usuario.getNome());
    }

    @Transactional
    public void deletar(Long id) {

        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        boolean possuiReservaAtiva =
                reservaRepository.existsByUsuarioIdAndStatus(id, ReservaStatus.ATIVA);

        if (possuiReservaAtiva) {
            throw new UsuarioComReservaAtivaException(
                    "Não é possível deletar o usuário, pois existem reservas ativas associadas a ele."
            );
        }
        usuarioRepository.delete(usuario);
    }

    @Transactional
    public UsuarioDto atualizar(UsuarioDto usuarioDto, Long id) {

        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        boolean possuiReservaAtiva =
                reservaRepository.existsByUsuarioIdAndStatus(id, ReservaStatus.ATIVA);

        if (possuiReservaAtiva) {
            throw new UsuarioComReservaAtivaException(
                    "Não é possível atualizar o usuário, pois existem reservas ativas associadas a ele."
            );
        }

        if(usuarioDto.nome() != null){
            usuario.setNome(usuarioDto.nome());
        }

        usuarioRepository.save(usuario);

        return new UsuarioDto(usuario.getNome());
    }

    public List<UsuarioDto> listar() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioDto(usuario.getNome()))
                .toList();
    }

    public UsuarioDto buscarPorId(Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        return new UsuarioDto(
                usuario.getNome()
        );
    }

}