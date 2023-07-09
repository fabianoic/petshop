package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.EnderecoDTO;
import br.com.ficampos.petshop.exception.DadoJaExisteException;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.Endereco;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.ficampos.petshop.util.PermissaoUtil.validaUsuario;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Endereco> buscaEnderecosPorClienteId(Long clienteId) {
        return enderecoRepository.findByClienteId(clienteId);
    }

    public List<Endereco> buscaEnderecosPorClienteUsuarioId(Long usuarioId) {
        return enderecoRepository.findByClienteUsuarioId(usuarioId);
    }

    public Endereco buscaEnderecoPorId(Long id) {
        Optional<Endereco> endereco = enderecoRepository.findById(id);
        if (endereco.isPresent()) {
            return endereco.get();
        }
        throw new DadoNaoExisteException(String.format("O Endereco com o ID: %s não foi encontrado!", id));
    }

    public Endereco criaEnderecoPorUsuarioId(EnderecoDTO EnderecoDTO, Long usuarioId) {
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        boolean usuarioProprioSolicitante = validaUsuario(EnderecoDTO.getCliente(), usuarioId);
        if (usuario.getPerfil().equals(Perfil.ADMIN) || usuarioProprioSolicitante) {
            usuario = usuarioProprioSolicitante ?
                    usuario : usuarioService.buscaUsuario(EnderecoDTO.getCliente().getUsuario().getId());
            Endereco endereco = new Endereco().fromDTO(EnderecoDTO);
            Cliente cliente = usuario.getCliente();
            if (cliente == null) {
                throw new DadoNaoExisteException("Usuário não é cliente para adicionar Endereco!");
            }
            endereco.setCliente(cliente);
            if (cliente.getEnderecos().stream()
                    .anyMatch(end -> end.getLogradouro().equals(EnderecoDTO.getLogradouro()) &&
                            end.getBairro().equals(EnderecoDTO.getBairro()) &&
                            end.getCidade().equals(EnderecoDTO.getCidade()) &&
                            end.getComplemento().equals(EnderecoDTO.getComplemento()))) {
                throw new DadoJaExisteException("Esse Endereco já foi adicionado anteriormente");
            }

            return enderecoRepository.save(endereco);
        }
        throw new UsuarioSemPermissaoException();
    }

    public Endereco editaEnderecoPorId(EnderecoDTO EnderecoDTO, Long usuarioId) {
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        Endereco endereco = buscaEnderecoPorId(EnderecoDTO.getId());
        if (endereco.getCliente().getUsuario().getId().equals(usuarioId) ||
                usuario.getPerfil().equals(Perfil.ADMIN)) {
            endereco.fromDTO(EnderecoDTO);
            enderecoRepository.save(endereco);
            return endereco;
        }
        throw new UsuarioSemPermissaoException();
    }


    public void deletaEnderecoPorId(Long id, Long usuarioId) {
        Endereco endereco = buscaEnderecoPorId(id);
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        if (endereco.getCliente().getUsuario().getId().equals(usuarioId) || usuario.getPerfil().equals(Perfil.ADMIN)) {
            enderecoRepository.delete(endereco);
            return;
        }
        throw new UsuarioSemPermissaoException();
    }
}
