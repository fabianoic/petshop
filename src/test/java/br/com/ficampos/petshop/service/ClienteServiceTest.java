package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.ClienteRepository;
import br.com.ficampos.petshop.repository.UsuarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Usuario usuario;

    private Cliente cliente;

    @Before
    public void setUp() {
        usuario = new Usuario();
        cliente = new Cliente();
    }

    @Test
    public void criaClienteDeveRetornarClienteCriado() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente cliente = clienteService.criaCliente(usuario);

        assertNotNull(cliente);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void buscaClientePorIdDeveRetornarCliente() {
        Long id = 1L;
        Optional<Cliente> optionalCliente = Optional.of(cliente);

        when(clienteRepository.findById(id)).thenReturn(optionalCliente);

        Cliente cliente = clienteService.buscaClientePorId(id);

        assertNotNull(cliente);
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void buscaClientePorIdDeveLancarDadoNaoExisteException() {
        Long id = 1L;
        Optional<Cliente> optionalCliente = Optional.empty();

        when(clienteRepository.findById(id)).thenReturn(optionalCliente);

        clienteService.buscaClientePorId(id);

        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    public void deletarCliente() {
        Long id = 1L;
        Optional<Cliente> optionalCliente = Optional.of(cliente);

        when(clienteRepository.findById(id)).thenReturn(optionalCliente);

        clienteService.deletarCliente(id);

        verify(clienteRepository, times(1)).delete(cliente);
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void deletarClienteDeveLancarDadoNaoExisteException() {
        Long id = 1L;
        Optional<Cliente> optionalCliente = Optional.empty();

        when(clienteRepository.findById(id)).thenReturn(optionalCliente);


        clienteService.deletarCliente(id);

        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, times(0)).delete(any());
    }
}

