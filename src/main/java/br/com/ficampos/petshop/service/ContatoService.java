package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.ContatoDTO;
import br.com.ficampos.petshop.exception.DadoInformadoInvalidoException;
import br.com.ficampos.petshop.exception.DadoJaExisteException;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.model.contato.Contato;
import br.com.ficampos.petshop.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static br.com.ficampos.petshop.util.PermissaoUtil.validaUsuario;

@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Contato> buscaContatosPorClienteId(Long clienteId) {
        return contatoRepository.findByClienteId(clienteId);
    }

    public List<Contato> buscaContatosPorClienteUsuarioId(Long usuarioId) {
        return contatoRepository.findByClienteUsuarioId(usuarioId);
    }

    public Contato buscaContatoPorId(Long id) {
        Optional<Contato> contato = contatoRepository.findById(id);
        if (contato.isPresent()) {
            return contato.get();
        }
        throw new DadoNaoExisteException(String.format("O contato com o ID: %s não foi encontrado!", id));
    }

    public Contato criaContatoPorClienteId(ContatoDTO contatoDTO, Long usuarioId) {
        validaContato(contatoDTO);
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        boolean usuarioProprioSolicitante = validaUsuario(contatoDTO.getCliente(), usuarioId);
        if (usuario.getPerfil().equals(Perfil.ADMIN) || usuarioProprioSolicitante) {
            usuario = usuarioProprioSolicitante ?
                    usuario : usuarioService.buscaUsuario(contatoDTO.getCliente().getUsuario().getId());
            Contato contato = new Contato().fromDTO(contatoDTO);
            Cliente cliente = usuario.getCliente();
            if (cliente == null) {
                throw new DadoNaoExisteException("Usuário não é cliente para adicionar contato!");
            }
            contato.setCliente(cliente);
            if (cliente.getContatos().stream()
                    .anyMatch(c -> c.getValor().equals(contatoDTO.getValor()))) {
                throw new DadoJaExisteException("Essa contato já foi adicionado anteriormente");
            }

            return contatoRepository.save(contato);
        }
        throw new UsuarioSemPermissaoException();
    }

    public Contato editaContatoPorId(ContatoDTO contatoDTO, Long usuarioId) {
        validaContato(contatoDTO);
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        Contato contato = buscaContatoPorId(contatoDTO.getId());
        if (contato.getCliente().getUsuario().getId().equals(usuarioId) ||
                usuario.getPerfil().equals(Perfil.ADMIN)) {
            contato.fromDTO(contatoDTO);
            contatoRepository.save(contato);
            return contato;
        }
        throw new UsuarioSemPermissaoException();
    }


    public void deletaContatoPorId(Long id, Long usuarioId) {
        Contato contato = buscaContatoPorId(id);
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        if (contato.getCliente().getUsuario().getId().equals(usuarioId) || usuario.getPerfil().equals(Perfil.ADMIN)) {
            contatoRepository.delete(contato);
            return;
        }
        throw new UsuarioSemPermissaoException();
    }

    private void validaContato(ContatoDTO contatoDTO) {
        String tipo = contatoDTO.getTipo().toUpperCase(Locale.ROOT);
        String valor = contatoDTO.getValor();

        if ((tipo.equals("EMAIL") && valor.contains("@")) ||
                (tipo.equals("TELEFONE") && valor.length() == 11)) {
            return;
        }
        throw new DadoInformadoInvalidoException();
    }

}
