package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.RacaDTO;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Raca;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.RacaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RacaServiceTest {

    @Mock
    private RacaRepository racaRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private RacaService racaService;

    private Usuario usuario;
    private RacaDTO racaDTO;
    private Raca raca;

    @Before
    public void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPerfil(Perfil.ADMIN);

        racaDTO = new RacaDTO();
        racaDTO.setId(1L);
        racaDTO.setNome("Rottweiler");

        raca = new Raca();
        raca.setId(1L);
        raca.setNome("Rottweiler");
    }

    @Test
    public void buscaRacas() {
        List<Raca> expectedRacas = new ArrayList<>();
        expectedRacas.add(raca);

        when(racaRepository.findAll()).thenReturn(expectedRacas);

        List<Raca> result = racaService.buscaRacas();

        assertEquals(expectedRacas, result);
        verify(racaRepository, times(1)).findAll();
    }

    @Test
    public void buscaRacaPorId() {
        Long racaId = 1L;
        Optional<Raca> optionalRaca = Optional.of(raca);

        when(racaRepository.findById(racaId)).thenReturn(optionalRaca);

        Raca result = racaService.buscaRacaPorId(racaId);

        assertEquals(raca, result);
        verify(racaRepository, times(1)).findById(racaId);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void buscaRacaPorIdDadoNaoExiste() {
        Long racaId = 1L;
        Optional<Raca> optionalRaca = Optional.empty();

        when(racaRepository.findById(racaId)).thenReturn(optionalRaca);

        racaService.buscaRacaPorId(racaId);
    }

    @Test
    public void criaRacaComPermissaoAdmin() {
        Long usuarioId = 1L;

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(racaRepository.save(any(Raca.class))).thenReturn(raca);

        Raca result = racaService.criaRaca(racaDTO, usuarioId);

        assertEquals(raca, result);
        verify(racaRepository, times(1)).save(any(Raca.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void criaRacaSemPermissao() {
        Long usuarioId = 1L;

        Usuario usuarioNaoAdmin = new Usuario();
        usuarioNaoAdmin.setId(2L);
        usuarioNaoAdmin.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuarioNaoAdmin);

        racaService.criaRaca(racaDTO, usuarioId);
    }

    @Test
    public void editaRacaPorIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        racaDTO.setNome("Nova Raça");

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(racaRepository.findById(racaDTO.getId())).thenReturn(Optional.of(raca));
        when(racaRepository.save(any(Raca.class))).thenReturn(raca);

        Raca result = racaService.editaRacaPorId(racaDTO, usuarioId);

        assertEquals(raca, result);
        assertEquals(racaDTO.getNome(), raca.getNome());
        verify(racaRepository, times(1)).save(any(Raca.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void editaRacaPorIdSemPermissao() {
        Long usuarioId = 1L;
        racaDTO.setNome("Nova Raça");

        Usuario usuarioNaoAdmin = new Usuario();
        usuarioNaoAdmin.setId(2L);
        usuarioNaoAdmin.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuarioNaoAdmin);

        racaService.editaRacaPorId(racaDTO, usuarioId);
    }

    @Test
    public void deletaRacaPorIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        Long racaId = 1L;

        when(racaRepository.findById(racaId)).thenReturn(Optional.of(raca));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        racaService.deletaRacaPorId(racaId, usuarioId);

        verify(racaRepository, times(1)).delete(raca);
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void deletaRacaPorIdSemPermissao() {
        Long usuarioId = 1L;
        Long racaId = 1L;

        Usuario usuarioNaoAdmin = new Usuario();
        usuarioNaoAdmin.setId(2L);
        usuarioNaoAdmin.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuarioNaoAdmin);

        racaService.deletaRacaPorId(racaId, usuarioId);
    }
}

