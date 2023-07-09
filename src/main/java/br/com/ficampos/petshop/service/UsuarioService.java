package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.autenticacao.UsuarioDTO;
import br.com.ficampos.petshop.exception.DadoJaExisteException;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

import static br.com.ficampos.petshop.util.PermissaoUtil.validaUsuario;

@Service
@Validated
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteService clienteService;

    public Usuario criaUsuario(@Valid UsuarioDTO usuarioDTO, Long idSolicitante) {
        if (usuarioRepository.existsUsuarioByCpf(usuarioDTO.getCpf())) {
            throw new DadoJaExisteException(String.format("Usuario com CPF %s ja cadastrado", usuarioDTO.getCpf()));
        }
        Usuario usuarioSolicitante = buscaUsuario(idSolicitante);
        if (!usuarioSolicitante.getPerfil().equals(Perfil.ADMIN)) {
            throw new UsuarioSemPermissaoException();
        }
        Usuario usuario = new Usuario().fromDTO(usuarioDTO);

        usuarioRepository.save(usuario);

        if (usuario.getPerfil().equals(Perfil.CLIENTE)) {
            clienteService.criaCliente(usuario);
        }
        return usuario;
    }

    public Usuario buscaUsuarioPeloCpf(String cpf) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCpf(String.valueOf(cpf));
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        }
        throw new DadoNaoExisteException(String.format("Usuario com CPF %s nao existe", cpf));
    }

    public Usuario buscaUsuario(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        }
        throw new DadoNaoExisteException(String.format("Usuario com ID %s nao existe", id));
    }

    public Usuario editarUsuario(UsuarioDTO usuarioDTO, Long idSolicitante) {
        Usuario usuarioSolicitante = buscaUsuario(idSolicitante);
        Usuario usuarioAlteracao = buscaUsuario(usuarioDTO.getId());
        if (usuarioSolicitante.getPerfil().equals(Perfil.ADMIN)) {
            if (usuarioDTO.getPerfil().equals(Perfil.CLIENTE) &&
                    usuarioAlteracao.getPerfil().equals(Perfil.ADMIN)) {
                clienteService.criaCliente(usuarioAlteracao);
            }

            if (isCampoValido(usuarioDTO.getNome())) {
                usuarioAlteracao.setNome(usuarioDTO.getNome());
            }
            if (isCampoValido(usuarioDTO.getSenha())) {
                usuarioAlteracao.setSenha(usuarioDTO.getSenha());
            }
            if (isCampoValido(usuarioDTO.getPerfil().getNome())) {
                usuarioAlteracao.setPerfil(Perfil.valueOf(usuarioDTO.getPerfil().name()));
            }
            if (isCampoValido(usuarioDTO.getCpf())) {
                usuarioAlteracao.setCpf(usuarioDTO.getCpf());
            }

            usuarioRepository.save(usuarioAlteracao);
            return usuarioAlteracao;
        } else if (usuarioSolicitante.getCpf().equals(usuarioDTO.getCpf())) {
            usuarioAlteracao.setNome(usuarioDTO.getNome());
            return usuarioRepository.save(usuarioAlteracao);
        }
        throw new UsuarioSemPermissaoException();
    }

    public void deletarUsuario(Long idSolicitante, Long idUsuarioDeletar) {
        Usuario usuarioSolicitante = buscaUsuario(idSolicitante);
        Usuario usuarioDeletar = buscaUsuario(idUsuarioDeletar);
        if (validaUsuario(usuarioDeletar.getCliente().toDTO(), idSolicitante) ||
                usuarioSolicitante.getPerfil().equals(Perfil.ADMIN)) {
            clienteService.deletarCliente(usuarioDeletar.getCliente().getId());
            return;
        }
        throw new UsuarioSemPermissaoException();
    }

    private boolean isCampoValido(String valor) {
        return valor != null && !valor.isBlank();
    }
}
