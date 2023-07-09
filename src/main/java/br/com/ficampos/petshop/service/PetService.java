package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.PetDTO;
import br.com.ficampos.petshop.exception.DadoJaExisteException;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.Pet;
import br.com.ficampos.petshop.model.Raca;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.ficampos.petshop.util.PermissaoUtil.validaUsuario;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RacaService racaService;

    public List<Pet> buscaPetsPorClienteId(Long clienteId) {
        return petRepository.findByClienteId(clienteId);
    }

    public List<Pet> buscaPetsPorClienteUsuarioId(Long usuarioId) {
        return petRepository.findByClienteUsuarioId(usuarioId);
    }

    public Pet buscaPetPorId(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            return pet.get();
        }
        throw new DadoNaoExisteException(String.format("O Pet com o ID: %s não foi encontrado!", id));
    }

    public Pet criaPetPorClienteId(PetDTO petDTO, Long usuarioId) {
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        boolean usuarioProprioSolicitante = validaUsuario(petDTO.getCliente(), usuarioId);
        if (usuario.getPerfil().equals(Perfil.ADMIN) || usuarioProprioSolicitante) {
            usuario = usuarioProprioSolicitante ?
                    usuario : usuarioService.buscaUsuario(petDTO.getCliente().getUsuario().getId());
            Cliente cliente = usuario.getCliente();
            if (cliente == null) {
                throw new DadoNaoExisteException("Usuário não é cliente para adicionar pet!");
            }
            Pet pet = new Pet().fromDTO(petDTO);
            Raca raca = racaService.buscaRacaPorId(petDTO.getRaca().getId());
            pet.setCliente(cliente);
            pet.setRaca(raca);
            if (cliente.getPets().stream()
                    .anyMatch(p -> p.getNome().equals(petDTO.getNome()) &&
                            p.getDtNascimento().equals(petDTO.getDtNascimento()) &&
                            p.getRaca().getId().equals(petDTO.getRaca().getId()))) {
                throw new DadoJaExisteException("Essa pet já foi adicionado anteriormente");
            }

            return petRepository.save(pet);
        }
        throw new UsuarioSemPermissaoException();
    }

    public Pet editaPetPorId(PetDTO petDTO, Long usuarioId) {
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        Pet pet = buscaPetPorId(petDTO.getId());
        if (pet.getCliente().getUsuario().getId().equals(usuarioId) ||
                usuario.getPerfil().equals(Perfil.ADMIN)) {
            pet.fromDTO(petDTO);
            Raca raca = racaService.buscaRacaPorId(petDTO.getRaca().getId());
            pet.setRaca(raca);
            petRepository.save(pet);
            return pet;
        }
        throw new UsuarioSemPermissaoException();
    }


    public void deletaPetPorId(Long id, Long usuarioId) {
        Pet pet = buscaPetPorId(id);
        Usuario usuario = usuarioService.buscaUsuario(usuarioId);
        if (pet.getCliente().getUsuario().getId().equals(usuarioId) || usuario.getPerfil().equals(Perfil.ADMIN)) {
            petRepository.delete(pet);
            return;
        }
        throw new UsuarioSemPermissaoException();
    }
}
