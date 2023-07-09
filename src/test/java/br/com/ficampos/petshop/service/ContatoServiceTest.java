package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.ClienteDTO;
import br.com.ficampos.petshop.dto.ContatoDTO;
import br.com.ficampos.petshop.dto.autenticacao.ConsultaUsuarioDTO;
import br.com.ficampos.petshop.exception.DadoInformadoInvalidoException;
import br.com.ficampos.petshop.exception.DadoJaExisteException;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.model.contato.Contato;
import br.com.ficampos.petshop.repository.ContatoRepository;
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
public class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ContatoService contatoService;

    private Usuario usuario;
    private Cliente cliente;
    private ClienteDTO clienteDTO;
    private ContatoDTO contatoDTO;
    private Contato contato;

    @Before
    public void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPerfil(Perfil.ADMIN);

        cliente = new Cliente();
        cliente.setUsuario(usuario);

        clienteDTO = new ClienteDTO();
        clienteDTO.setUsuario(new ConsultaUsuarioDTO(1L));

        contatoDTO = new ContatoDTO();
        contatoDTO.setId(1L);

        contato = new Contato();
        contato.setId(1L);
        contato.setCliente(cliente);
    }

    @Test
    public void buscaContatosPorClienteId() {
        Long clienteId = 1L;
        List<Contato> expectedContatos = new ArrayList<>();
        expectedContatos.add(contato);

        when(contatoRepository.findByClienteId(clienteId)).thenReturn(expectedContatos);

        List<Contato> result = contatoService.buscaContatosPorClienteId(clienteId);

        assertEquals(expectedContatos, result);
        verify(contatoRepository, times(1)).findByClienteId(clienteId);
    }

    @Test
    public void buscaContatosPorClienteUsuarioId() {
        Long usuarioId = 1L;
        List<Contato> expectedContatos = new ArrayList<>();
        expectedContatos.add(contato);

        when(contatoRepository.findByClienteUsuarioId(usuarioId)).thenReturn(expectedContatos);

        List<Contato> result = contatoService.buscaContatosPorClienteUsuarioId(usuarioId);

        assertEquals(expectedContatos, result);
        verify(contatoRepository, times(1)).findByClienteUsuarioId(usuarioId);
    }

    @Test
    public void buscaContatoPorId() {
        Long contatoId = 1L;
        Optional<Contato> optionalContato = Optional.of(contato);

        when(contatoRepository.findById(contatoId)).thenReturn(optionalContato);

        Contato result = contatoService.buscaContatoPorId(contatoId);

        assertEquals(contato, result);
        verify(contatoRepository, times(1)).findById(contatoId);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void buscaContatoPorIdDadoNaoExiste() {
        Long contatoId = 1L;
        Optional<Contato> optionalContato = Optional.empty();

        when(contatoRepository.findById(contatoId)).thenReturn(optionalContato);

        contatoService.buscaContatoPorId(contatoId);

        verify(contatoRepository, times(1)).findById(contatoId);
    }

    @Test(expected = DadoInformadoInvalidoException.class)
    public void criaContatoPorClienteIdDadoInformadoInvalido() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("VALOR");
        contatoDTO.setValor("teste");

        usuario.setCliente(cliente);

        contatoService.criaContatoPorClienteId(contatoDTO, usuarioId);
    }

    @Test
    public void criaContatoPorClienteIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("EMAIL");
        contatoDTO.setValor("email@teste.com");

        usuario.setCliente(cliente);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato result = contatoService.criaContatoPorClienteId(contatoDTO, usuarioId);

        assertEquals(contato, result);
        verify(contatoRepository, times(1)).save(any(Contato.class));
    }

    @Test
    public void criaContatoPorClienteIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("EMAIL");
        contatoDTO.setValor("email@teste.com");

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        usuarioProprioSolicitante.setCliente(cliente);
        usuarioProprioSolicitante.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(usuarioService.buscaUsuario(contatoDTO.getCliente().getUsuario().getId())).thenReturn(usuarioProprioSolicitante);
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato result = contatoService.criaContatoPorClienteId(contatoDTO, usuarioId);

        assertEquals(contato, result);
        verify(contatoRepository, times(1)).save(any(Contato.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void criaContatoPorClienteIdSemPermissao() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("EMAIL");
        contatoDTO.setValor("email@teste.com");
        contatoDTO.getCliente().getUsuario().setId(2L);

        usuario.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        contatoService.criaContatoPorClienteId(contatoDTO, usuarioId);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void criaContatoPorClienteIdSemCliente() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("EMAIL");
        contatoDTO.setValor("email@teste.com");
        usuario.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        contatoService.criaContatoPorClienteId(contatoDTO, usuarioId);
    }

    @Test(expected = DadoJaExisteException.class)
    public void criaContatoPorClienteIdContatoJaExiste() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("TELEFONE");
        contatoDTO.setValor("53981389666");

        contatoDTO.setTipo("TELEFONE");
        contato.setValor("53981389666");
        cliente.setContatos(new ArrayList<>());
        cliente.getContatos().add(contato);
        usuario.setCliente(cliente);
        usuario.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        contatoService.criaContatoPorClienteId(contatoDTO, usuarioId);
    }

    @Test
    public void editaContatoPorIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("EMAIL");
        contatoDTO.setValor("novoemail@teste.com");

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(contatoRepository.findById(contatoDTO.getId())).thenReturn(Optional.of(contato));
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato result = contatoService.editaContatoPorId(contatoDTO, usuarioId);

        assertEquals(contato, result);
        assertEquals(contatoDTO.getValor(), contato.getValor());
        verify(contatoRepository, times(1)).save(any(Contato.class));
    }

    @Test
    public void editaContatoPorIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("EMAIL");
        contatoDTO.setValor("novoemail@teste.com");

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        cliente.setUsuario(usuarioProprioSolicitante);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(contatoRepository.findById(contatoDTO.getId())).thenReturn(Optional.of(contato));
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato result = contatoService.editaContatoPorId(contatoDTO, usuarioId);

        assertEquals(contato, result);
        assertEquals(contatoDTO.getValor(), contato.getValor());
        verify(contatoRepository, times(1)).save(any(Contato.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void editaContatoPorIdSemPermissao() {
        Long usuarioId = 1L;
        contatoDTO.setCliente(clienteDTO);
        contatoDTO.setTipo("EMAIL");
        contatoDTO.setValor("novoemail@teste.com");

        usuario.setPerfil(Perfil.CLIENTE);
        usuario.setId(2L);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(contatoRepository.findById(contatoDTO.getId())).thenReturn(Optional.of(contato));

        contatoService.editaContatoPorId(contatoDTO, usuarioId);
    }

    @Test
    public void deletaContatoPorIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        Long contatoId = 1L;

        when(contatoRepository.findById(contatoId)).thenReturn(Optional.of(contato));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        contatoService.deletaContatoPorId(contatoId, usuarioId);

        verify(contatoRepository, times(1)).delete(contato);
    }

    @Test
    public void deletaContatoPorIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;
        Long contatoId = 1L;

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        cliente.setUsuario(usuarioProprioSolicitante);

        when(contatoRepository.findById(contatoId)).thenReturn(Optional.of(contato));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        contatoService.deletaContatoPorId(contatoId, usuarioId);

        verify(contatoRepository, times(1)).delete(contato);
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void deletaContatoPorIdSemPermissao() {
        Long usuarioId = 1L;
        Long contatoId = 1L;

        usuario.setPerfil(Perfil.CLIENTE);
        usuario.setId(2L);

        when(contatoRepository.findById(contatoId)).thenReturn(Optional.of(contato));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        contatoService.deletaContatoPorId(contatoId, usuarioId);
    }
}

