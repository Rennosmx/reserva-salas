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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private ReservaService reservaService;


    @Test
    void deveCriarReservaComSucesso() {

        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 12, 0);

        Usuario usuario = new Usuario("João");
        Sala sala = new Sala("Sala 1", 10);

        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        ReflectionTestUtils.setField(sala, "id", salaId);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuario));

        when(salaRepository.findById(salaId))
                .thenReturn(Optional.of(sala));

        when(reservaRepository.existeConflitoComUsuario(
                null, usuarioId, ReservaStatus.ATIVA, inicio, fim))
                .thenReturn(false);

        when(reservaRepository.existeConflitoNaSala(
                null, salaId, ReservaStatus.ATIVA, inicio, fim))
                .thenReturn(false);

        when(reservaRepository.save(any(Reserva.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservaDto resultado = reservaService.criar(dto);

        assertNotNull(resultado);
        assertEquals(inicio, resultado.dataInicio());
        assertEquals(fim, resultado.dataFim());
        assertEquals(ReservaStatus.ATIVA, resultado.status());
        assertEquals(usuarioId, resultado.usuario());
        assertEquals(salaId, resultado.sala());

        verify(reservaRepository).save(any(Reserva.class));
    }


    @Test
    void deveImpedirReservaQuandoUsuarioTemConflitoDeHorario() {

        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 12, 0);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        when(reservaRepository.existeConflitoComUsuario(
                null, usuarioId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> reservaService.criar(dto)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }


    @Test
    void deveImpedirReservaQuandoSalaTemConflitoDeHorario() {

        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 12, 0);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        when(reservaRepository.existeConflitoNaSala(
                null, salaId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> reservaService.criar(dto)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }


    @Test
    void devePermitirReservaQuandoNovaReservaComecaExatamenteNoFimDaAnterior() {

        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 12, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 14, 0);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(new Usuario("João")));

        when(salaRepository.findById(salaId))
                .thenReturn(Optional.of(new Sala("Sala 1", 10)));

        when(reservaRepository.existeConflitoComUsuario(
                null, usuarioId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(false);

        when(reservaRepository.existeConflitoNaSala(
                null, salaId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(false);

        when(reservaRepository.save(any(Reserva.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> reservaService.criar(dto));

        verify(reservaRepository).save(any(Reserva.class));
    }


    @Test
    void devePermitirReservaQuandoNovaReservaTerminaExatamenteNoInicioDaAnterior() {

        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 12, 0);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(new Usuario("João")));

        when(salaRepository.findById(salaId))
                .thenReturn(Optional.of(new Sala("Sala 1", 10)));

        when(reservaRepository.existeConflitoComUsuario(
                null, usuarioId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(false);

        when(reservaRepository.existeConflitoNaSala(
                null, salaId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(false);

        when(reservaRepository.save(any(Reserva.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> reservaService.criar(dto));

        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void deveImpedirReservaQuandoDataInicioNaoForAnteriorADataFim() {

        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 12, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 10, 0);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        assertThrows(
                ReservaHorarioInvalidoException.class,
                () -> reservaService.criar(dto)
        );

        verifyNoInteractions(
                usuarioRepository,
                salaRepository,
                reservaRepository
        );
    }

    @Test
    void deveImpedirReservaQuandoUsuarioNaoExiste() {

        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 12, 0);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        when(reservaRepository.existeConflitoNaSala(
                null, salaId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(false);

        when(reservaRepository.existeConflitoComUsuario(
                null, usuarioId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(false);

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNotFoundException.class,
                () -> reservaService.criar(dto)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void deveImpedirReservaQuandoSalaNaoExiste() {

        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 12, 0);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        when(reservaRepository.existeConflitoNaSala(
                null, salaId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(false);

        when(reservaRepository.existeConflitoComUsuario(
                null, usuarioId, ReservaStatus.ATIVA, inicio, fim
        )).thenReturn(false);

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(new Usuario("João")));

        when(salaRepository.findById(salaId))
                .thenReturn(Optional.empty());

        assertThrows(
                SalaNotFoundException.class,
                () -> reservaService.criar(dto)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void deveLancarExcecaoQuandoReservaNaoExiste() {

        Long reservaId = 1L;

        when(reservaRepository.findById(reservaId))
                .thenReturn(Optional.empty());

        assertThrows(
                ReservaNotFoundException.class,
                () -> reservaService.buscarPorId(reservaId)
        );
    }

    @Test
    void deveImpedirCancelamentoDeReservaNaoAtiva() {

        Long reservaId = 1L;

        Usuario usuario = new Usuario("João");
        Sala sala = new Sala("Sala 1", 10);

        Reserva reserva = new Reserva(
                LocalDateTime.of(2026, 9, 1, 10, 0),
                LocalDateTime.of(2026, 9, 1, 12, 0),
                usuario,
                sala
        );

        reserva.setStatus(ReservaStatus.CANCELADA);

        when(reservaRepository.findById(reservaId))
                .thenReturn(Optional.of(reserva));

        assertThrows(
                ReservaAlterarException.class,
                () -> reservaService.cancelarReserva(reservaId)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void deveImpedirConclusaoDeReservaNaoAtiva() {

        Long reservaId = 1L;

        Usuario usuario = new Usuario("João");
        Sala sala = new Sala("Sala 1", 10);

        Reserva reserva = new Reserva(
                LocalDateTime.of(2026, 9, 1, 10, 0),
                LocalDateTime.of(2026, 9, 1, 12, 0),
                usuario,
                sala
        );

        reserva.setStatus(ReservaStatus.CANCELADA);

        when(reservaRepository.findById(reservaId))
                .thenReturn(Optional.of(reserva));

        assertThrows(
                ReservaAlterarException.class,
                () -> reservaService.concluirReserva(reservaId)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void deveImpedirAtualizacaoDeReservaNaoAtiva() {

        Long reservaId = 1L;
        Long usuarioId = 1L;
        Long salaId = 1L;

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 9, 1, 12, 0);

        Usuario usuario = new Usuario("João");
        Sala sala = new Sala("Sala 1", 10);

        Reserva reserva = new Reserva(
                inicio,
                fim,
                usuario,
                sala
        );

        reserva.setStatus(ReservaStatus.CANCELADA);

        ReservaDto dto = new ReservaDto(
                inicio,
                fim,
                ReservaStatus.ATIVA,
                usuarioId,
                salaId
        );

        when(reservaRepository.findById(reservaId))
                .thenReturn(Optional.of(reserva));

        assertThrows(
                ReservaAlterarException.class,
                () -> reservaService.atualizar(dto, reservaId)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

}