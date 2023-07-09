package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.ClienteRepository;
import br.com.ficampos.petshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Cliente criaCliente(Usuario usuario) {
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        return clienteRepository.save(cliente);
    }

    public Cliente buscaClientePorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElse(null);
    }

    public void deletarCliente(Long id) {
        Cliente cliente = buscaClientePorId(id);
        clienteRepository.delete(cliente);
    }
}
