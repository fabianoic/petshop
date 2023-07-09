package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.RacaDTO;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Raca;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.RacaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RacaService {

    @Autowired
    private RacaRepository racaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Raca> buscaRacas() {
        return racaRepository.findAll();
    }

    public Raca buscaRacaPorId(Long id) {
        Optional<Raca> raca = racaRepository.findById(id);
        if (raca.isPresent()) {
            return raca.get();
        }
        throw new DadoNaoExisteException(String.format("A Raça com o ID: %s não foi encontrado!", id));
    }

    public Raca criaRaca(RacaDTO racaDTO, Long usuarioId) {
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        if (usuario.getPerfil().equals(Perfil.ADMIN)) {
            Raca raca = new Raca().fromDTO(racaDTO);
            return racaRepository.save(raca);
        }
        throw new UsuarioSemPermissaoException();
    }

    public Raca editaRacaPorId(RacaDTO racaDTO, Long usuarioId) {
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        if (usuario.getPerfil().equals(Perfil.ADMIN)) {
            Raca raca = buscaRacaPorId(racaDTO.getId());
            raca.fromDTO(racaDTO);
            racaRepository.save(raca);
            return raca;
        }
        throw new UsuarioSemPermissaoException();
    }


    public void deletaRacaPorId(Long id, Long usuarioId) {
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        if (usuario.getPerfil().equals(Perfil.ADMIN)) {
            Raca raca = buscaRacaPorId(id);
            racaRepository.delete(raca);
            return;
        }
        throw new UsuarioSemPermissaoException();
    }
}
