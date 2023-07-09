package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.ClienteDTO;
import br.com.ficampos.petshop.dto.EnderecoDTO;
import br.com.ficampos.petshop.dto.autenticacao.ConsultaUsuarioDTO;
import br.com.ficampos.petshop.exception.DadoJaExisteException;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.Endereco;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.EnderecoRepository;
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
public class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private EnderecoService enderecoService;

    private Usuario usuario;
    private Cliente cliente;
    private ClienteDTO clienteDTO;
    private EnderecoDTO enderecoDTO;
    private Endereco endereco;

    @Before
    public void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPerfil(Perfil.ADMIN);

        cliente = new Cliente();
        cliente.setUsuario(usuario);

        clienteDTO = new ClienteDTO();
        clienteDTO.setUsuario(new ConsultaUsuarioDTO(1L));

        enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(1L);

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCliente(cliente);
    }

    @Test
    public void buscaEnderecosPorClienteId() {
        Long clienteId = 1L;
        List<Endereco> expectedEnderecos = new ArrayList<>();
        expectedEnderecos.add(endereco);

        when(enderecoRepository.findByClienteId(clienteId)).thenReturn(expectedEnderecos);

        List<Endereco> result = enderecoService.buscaEnderecosPorClienteId(clienteId);

        assertEquals(expectedEnderecos, result);
        verify(enderecoRepository, times(1)).findByClienteId(clienteId);
    }

    @Test
    public void buscaEnderecosPorClienteUsuarioId() {
        Long usuarioId = 1L;
        List<Endereco> expectedEnderecos = new ArrayList<>();
        expectedEnderecos.add(endereco);

        when(enderecoRepository.findByClienteUsuarioId(usuarioId)).thenReturn(expectedEnderecos);

        List<Endereco> result = enderecoService.buscaEnderecosPorClienteUsuarioId(usuarioId);

        assertEquals(expectedEnderecos, result);
        verify(enderecoRepository, times(1)).findByClienteUsuarioId(usuarioId);
    }

    @Test
    public void buscaEnderecoPorId() {
        Long enderecoId = 1L;
        Optional<Endereco> optionalEndereco = Optional.of(endereco);

        when(enderecoRepository.findById(enderecoId)).thenReturn(optionalEndereco);

        Endereco result = enderecoService.buscaEnderecoPorId(enderecoId);

        assertEquals(endereco, result);
        verify(enderecoRepository, times(1)).findById(enderecoId);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void buscaEnderecoPorIdDadoNaoExiste() {
        Long enderecoId = 1L;
        Optional<Endereco> optionalEndereco = Optional.empty();

        when(enderecoRepository.findById(enderecoId)).thenReturn(optionalEndereco);

        enderecoService.buscaEnderecoPorId(enderecoId);
    }

    @Test
    public void criaEnderecoPorUsuarioIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        enderecoDTO.setCliente(clienteDTO);
        enderecoDTO.setLogradouro("Rua Teste");
        usuario.setPerfil(Perfil.ADMIN);
        usuario.setCliente(cliente);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco result = enderecoService.criaEnderecoPorUsuarioId(enderecoDTO, usuarioId);

        assertEquals(endereco, result);
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    public void criaEnderecoPorUsuarioIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;
        enderecoDTO.setCliente(clienteDTO);
        enderecoDTO.setLogradouro("Rua Teste");
        usuario.setPerfil(Perfil.CLIENTE);

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(1L);
        usuarioProprioSolicitante.setPerfil(Perfil.CLIENTE);
        usuarioProprioSolicitante.setCliente(cliente);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        when(usuarioService.buscaUsuario(enderecoDTO.getCliente().getUsuario().getId())).thenReturn(usuarioProprioSolicitante);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco result = enderecoService.criaEnderecoPorUsuarioId(enderecoDTO, usuarioId);

        assertEquals(endereco, result);
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void criaEnderecoPorUsuarioIdSemPermissao() {
        Long usuarioId = 1L;
        clienteDTO.setUsuario(new ConsultaUsuarioDTO(2L));
        enderecoDTO.setCliente(clienteDTO);
        enderecoDTO.setLogradouro("Rua Teste");
        usuario.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        enderecoService.criaEnderecoPorUsuarioId(enderecoDTO, usuarioId);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void criaEnderecoPorUsuarioIdSemCliente() {
        Long usuarioId = 1L;
        enderecoDTO.setCliente(clienteDTO);
        enderecoDTO.setLogradouro("Rua Teste");
        enderecoDTO.setCliente(clienteDTO);
        usuario.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        enderecoService.criaEnderecoPorUsuarioId(enderecoDTO, usuarioId);
    }

    @Test(expected = DadoJaExisteException.class)
    public void criaEnderecoPorUsuarioIdEnderecoJaExiste() {
        Long usuarioId = 1L;
        enderecoDTO.setCliente(clienteDTO);
        enderecoDTO.setLogradouro("Rua Teste");
        enderecoDTO.setBairro("Bairro Teste");
        enderecoDTO.setComplemento("Complemento Teste");
        enderecoDTO.setCidade("Cidade Teste");
        usuario.setPerfil(Perfil.CLIENTE);
        endereco.setLogradouro("Rua Teste");
        endereco.setBairro("Bairro Teste");
        endereco.setComplemento("Complemento Teste");
        endereco.setCidade("Cidade Teste");
        cliente.setEnderecos(new ArrayList<>());
        cliente.getEnderecos().add(endereco);
        usuario.setCliente(cliente);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        enderecoService.criaEnderecoPorUsuarioId(enderecoDTO, usuarioId);
    }

    @Test
    public void editaEnderecoPorIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        enderecoDTO.setCliente(clienteDTO);
        enderecoDTO.setLogradouro("Nova Rua Teste");

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(enderecoRepository.findById(enderecoDTO.getId())).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco result = enderecoService.editaEnderecoPorId(enderecoDTO, usuarioId);

        assertEquals(endereco, result);
        assertEquals(enderecoDTO.getLogradouro(), endereco.getLogradouro());
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    public void editaEnderecoPorIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;
        enderecoDTO.setCliente(clienteDTO);
        enderecoDTO.setLogradouro("Nova Rua Teste");

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        cliente.setUsuario(usuarioProprioSolicitante);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(enderecoRepository.findById(enderecoDTO.getId())).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco result = enderecoService.editaEnderecoPorId(enderecoDTO, usuarioId);

        assertEquals(endereco, result);
        assertEquals(enderecoDTO.getLogradouro(), endereco.getLogradouro());
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void editaEnderecoPorIdSemPermissao() {
        Long usuarioId = 1L;
        enderecoDTO.setCliente(clienteDTO);
        enderecoDTO.setLogradouro("Nova Rua Teste");

        endereco.getCliente().getUsuario().setId(2L);

        usuario.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(enderecoRepository.findById(enderecoDTO.getId())).thenReturn(Optional.of(endereco));

        enderecoService.editaEnderecoPorId(enderecoDTO, usuarioId);
    }

    @Test
    public void deletaEnderecoPorIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        Long enderecoId = 1L;

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        enderecoService.deletaEnderecoPorId(enderecoId, usuarioId);

        verify(enderecoRepository, times(1)).delete(endereco);
    }

    @Test
    public void deletaEnderecoPorIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;
        Long enderecoId = 1L;

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        cliente.setUsuario(usuarioProprioSolicitante);

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        enderecoService.deletaEnderecoPorId(enderecoId, usuarioId);

        verify(enderecoRepository, times(1)).delete(endereco);
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void deletaEnderecoPorIdSemPermissao() {
        Long usuarioId = 1L;
        Long enderecoId = 1L;

        usuario.setPerfil(Perfil.CLIENTE);
        endereco.getCliente().getUsuario().setId(2L);

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        enderecoService.deletaEnderecoPorId(enderecoId, usuarioId);
    }
}

