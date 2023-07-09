package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.autenticacao.UsuarioDTO;
import br.com.ficampos.petshop.exception.CPFInvalidoException;
import br.com.ficampos.petshop.exception.DadoJaExisteException;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.UsuarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private Usuario solicitanteAdmin;
    private Usuario solicitanteUsuario;

    @Before
    public void setup() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCpf("02968413074");
        usuarioDTO.setNome("Teste nome");
        usuarioDTO.setSenha("password");
        usuarioDTO.setPerfil(Perfil.CLIENTE);

        usuario = new Usuario();
        usuario.setCpf("02968413074");
        usuario.setNome("Teste nome");
        usuario.setSenha("password");
        usuario.setPerfil(Perfil.CLIENTE);

        solicitanteAdmin = new Usuario();
        solicitanteAdmin.setId(2L);
        solicitanteAdmin.setPerfil(Perfil.ADMIN);

        solicitanteUsuario = new Usuario();
        solicitanteUsuario.setId(3L);
        solicitanteUsuario.setPerfil(Perfil.CLIENTE);
        solicitanteUsuario.setCpf("02968413074");
    }

    @Test
    public void criaUsuarioComPermissaoAdmin() {
        Long solicitanteId = 2L;

        when(usuarioRepository.existsUsuarioByCpf(usuarioDTO.getCpf())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(solicitanteAdmin));

        Usuario result = usuarioService.criaUsuario(usuarioDTO, solicitanteId);

        assertEquals(usuario, result);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(clienteService, times(1)).criaCliente(usuario);
    }

    @Test(expected = CPFInvalidoException.class)
    public void criaUsuarioCpfInvalido() {
        Long solicitanteId = 2L;
        usuarioDTO.setCpf("12345678910");

        when(usuarioRepository.existsUsuarioByCpf(usuarioDTO.getCpf())).thenReturn(false);
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(solicitanteAdmin));

        usuarioService.criaUsuario(usuarioDTO, solicitanteId);
    }

    @Test(expected = DadoJaExisteException.class)
    public void criaUsuarioComCpfExistente() {
        Long solicitanteId = 2L;

        when(usuarioRepository.existsUsuarioByCpf(usuarioDTO.getCpf())).thenReturn(true);

        usuarioService.criaUsuario(usuarioDTO, solicitanteId);
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void criaUsuarioSemPermissao() {
        Long solicitanteId = 3L;

        when(usuarioRepository.existsUsuarioByCpf(usuarioDTO.getCpf())).thenReturn(false);
        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteUsuario));

        usuarioService.criaUsuario(usuarioDTO, solicitanteId);
    }

    @Test
    public void buscaUsuarioPeloCpf() {
        String cpf = "02968413074";
        Optional<Usuario> optionalUsuario = Optional.of(usuario);

        when(usuarioRepository.findByCpf(cpf)).thenReturn(optionalUsuario);

        Usuario result = usuarioService.buscaUsuarioPeloCpf(cpf);

        assertEquals(usuario, result);
        verify(usuarioRepository, times(1)).findByCpf(cpf);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void buscaUsuarioPeloCpfDadoNaoExiste() {
        String cpf = "02968413074";
        Optional<Usuario> optionalUsuario = Optional.empty();

        when(usuarioRepository.findByCpf(cpf)).thenReturn(optionalUsuario);

        usuarioService.buscaUsuarioPeloCpf(cpf);
    }

    @Test
    public void buscaUsuario() {
        Long usuarioId = 1L;
        Optional<Usuario> optionalUsuario = Optional.of(usuario);

        when(usuarioRepository.findById(usuarioId)).thenReturn(optionalUsuario);

        Usuario result = usuarioService.buscaUsuario(usuarioId);

        assertEquals(usuario, result);
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void buscaUsuarioDadoNaoExiste() {
        Long usuarioId = 1L;
        Optional<Usuario> optionalUsuario = Optional.empty();

        when(usuarioRepository.findById(usuarioId)).thenReturn(optionalUsuario);

        usuarioService.buscaUsuario(usuarioId);
    }

    @Test
    public void editarUsuarioComPermissaoAdmin() {
        Long solicitanteId = 2L;

        usuario.setPerfil(Perfil.ADMIN);

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteAdmin));
        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.editarUsuario(usuarioDTO, solicitanteId);

        assertEquals(usuario, result);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(clienteService, times(1)).criaCliente(any(Usuario.class));
    }

    @Test
    public void editarUsuarioComPermissaoCliente() {
        Long solicitanteId = 2L;

        usuario.setPerfil(Perfil.ADMIN);

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteUsuario));
        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.editarUsuario(usuarioDTO, solicitanteId);

        assertEquals(usuario, result);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(clienteService, never()).criaCliente(any(Usuario.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void editarUsuarioSemPermissao() {
        Long solicitanteId = 3L;

        usuarioDTO.setId(1L);
        usuarioDTO.setCpf("75195173060");

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteUsuario));
        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));

        usuarioService.editarUsuario(usuarioDTO, solicitanteId);
    }

    @Test
    public void editarUsuarioAlterandoNome() {
        Long solicitanteId = 2L;
        usuarioDTO.setNome("New Name");

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteAdmin));
        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.editarUsuario(usuarioDTO, solicitanteId);

        assertEquals(usuario, result);
        assertEquals(usuarioDTO.getNome(), usuario.getNome());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(clienteService, never()).criaCliente(any(Usuario.class));
    }

    @Test
    public void editarUsuarioAlterandoSenha() {
        Long solicitanteId = 2L;
        usuarioDTO.setSenha("newpassword");

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteAdmin));
        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.editarUsuario(usuarioDTO, solicitanteId);

        assertEquals(usuario, result);
        assertEquals(usuarioDTO.getSenha(), usuario.getSenha());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(clienteService, never()).criaCliente(any(Usuario.class));
    }

    @Test
    public void editarUsuarioAlterandoPerfil() {
        Long solicitanteId = 2L;
        usuarioDTO.setPerfil(Perfil.CLIENTE);

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteAdmin));
        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.editarUsuario(usuarioDTO, solicitanteId);

        assertEquals(usuario, result);
        assertEquals(usuarioDTO.getPerfil(), usuario.getPerfil());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(clienteService, never()).criaCliente(any(Usuario.class));
    }

    @Test
    public void editarUsuarioAlterandoCpf() {
        Long solicitanteId = 2L;
        usuarioDTO.setCpf("98765432100");

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteAdmin));
        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.editarUsuario(usuarioDTO, solicitanteId);

        assertEquals(usuario, result);
        assertEquals(usuarioDTO.getCpf(), usuario.getCpf());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(clienteService, never()).criaCliente(any(Usuario.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void editarUsuarioComCpfInexistente() {
        Long solicitanteId = 2L;

        usuarioDTO.setId(1L);
        usuarioDTO.setCpf("75195173060");

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteUsuario));
        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));

        usuarioService.editarUsuario(usuarioDTO, solicitanteId);
    }

    @Test
    public void deletarUsuarioComPermissaoAdmin() {
        Long solicitanteId = 2L;
        Long usuarioDeletarId = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setUsuario(usuario);
        cliente.setPets(new ArrayList<>());
        usuario.setCliente(cliente);

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteAdmin));
        when(usuarioRepository.findById(usuarioDeletarId)).thenReturn(Optional.of(usuario));

        usuarioService.deletarUsuario(solicitanteId, usuarioDeletarId);

        verify(clienteService, times(1)).deletarCliente(usuario.getCliente().getId());
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void deletarUsuarioSemPermissao() {
        Long solicitanteId = 1L;
        Long usuarioDeletarId = 2L;

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setUsuario(usuario);
        cliente.setPets(new ArrayList<>());
        usuario.setCliente(cliente);

        when(usuarioRepository.findById(solicitanteId)).thenReturn(Optional.of(solicitanteUsuario));
        when(usuarioRepository.findById(usuarioDeletarId)).thenReturn(Optional.of(usuario));

        usuarioService.deletarUsuario(solicitanteId, usuarioDeletarId);
    }
}

