package br.com.ficampos.petshop.service;

import br.com.ficampos.petshop.dto.ClienteDTO;
import br.com.ficampos.petshop.dto.PetDTO;
import br.com.ficampos.petshop.dto.RacaDTO;
import br.com.ficampos.petshop.dto.autenticacao.ConsultaUsuarioDTO;
import br.com.ficampos.petshop.exception.DadoJaExisteException;
import br.com.ficampos.petshop.exception.DadoNaoExisteException;
import br.com.ficampos.petshop.exception.UsuarioSemPermissaoException;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.Pet;
import br.com.ficampos.petshop.model.Raca;
import br.com.ficampos.petshop.model.autenticacao.Perfil;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.repository.PetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private RacaService racaService;

    @InjectMocks
    private PetService petService;

    private Usuario usuario;
    private Cliente cliente;
    private ClienteDTO clienteDTO;
    private Raca raca;
    private RacaDTO racaDTO;
    private PetDTO petDTO;
    private Pet pet;

    @Before
    public void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPerfil(Perfil.ADMIN);

        cliente = new Cliente();
        cliente.setUsuario(usuario);

        clienteDTO = new ClienteDTO();
        clienteDTO.setUsuario(new ConsultaUsuarioDTO(1L));

        raca = new Raca();
        raca.setId(1L);
        raca.setNome("Pitbull");

        racaDTO = new RacaDTO();
        racaDTO.setId(1L);
        racaDTO.setNome("Pitbull");

        petDTO = new PetDTO();
        petDTO.setId(1L);
        petDTO.setCliente(clienteDTO);
        petDTO.setRaca(racaDTO);
        petDTO.setNome("teste");
        petDTO.setDtNascimento(new Date());

        pet = new Pet();
        pet.setId(1L);
        pet.setCliente(cliente);
        pet.setRaca(raca);
        pet.setNome("teste");
        pet.setDtNascimento(new Date());
    }

    @Test
    public void buscaPetsPorClienteId() {
        Long clienteId = 1L;
        List<Pet> expectedPets = new ArrayList<>();
        expectedPets.add(pet);

        when(petRepository.findByClienteId(clienteId)).thenReturn(expectedPets);

        List<Pet> result = petService.buscaPetsPorClienteId(clienteId);

        assertEquals(expectedPets, result);
        verify(petRepository, times(1)).findByClienteId(clienteId);
    }

    @Test
    public void buscaPetsPorClienteUsuarioId() {
        Long usuarioId = 1L;
        List<Pet> expectedPets = new ArrayList<>();
        expectedPets.add(pet);

        when(petRepository.findByClienteUsuarioId(usuarioId)).thenReturn(expectedPets);

        List<Pet> result = petService.buscaPetsPorClienteUsuarioId(usuarioId);

        assertEquals(expectedPets, result);
        verify(petRepository, times(1)).findByClienteUsuarioId(usuarioId);
    }

    @Test
    public void buscaPetPorId() {
        Long petId = 1L;
        Optional<Pet> optionalPet = Optional.of(pet);

        when(petRepository.findById(petId)).thenReturn(optionalPet);

        Pet result = petService.buscaPetPorId(petId);

        assertEquals(pet, result);
        verify(petRepository, times(1)).findById(petId);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void buscaPetPorIdDadoNaoExiste() {
        Long petId = 1L;
        Optional<Pet> optionalPet = Optional.empty();

        when(petRepository.findById(petId)).thenReturn(optionalPet);

        petService.buscaPetPorId(petId);
    }

    @Test
    public void criaPetPorClienteIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        usuario.setCliente(cliente);
        cliente.setPets(new ArrayList<>());

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(racaService.buscaRacaPorId(petDTO.getRaca().getId())).thenReturn(raca);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet result = petService.criaPetPorClienteId(petDTO, usuarioId);

        assertEquals(pet, result);
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    public void criaPetPorClienteIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        cliente.setUsuario(usuarioProprioSolicitante);
        cliente.setPets(new ArrayList<>());
        usuarioProprioSolicitante.setPerfil(Perfil.CLIENTE);
        usuarioProprioSolicitante.setCliente(cliente);

        when(usuarioService.buscaUsuario(petDTO.getCliente().getUsuario().getId())).thenReturn(usuarioProprioSolicitante);
        when(racaService.buscaRacaPorId(petDTO.getRaca().getId())).thenReturn(raca);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet result = petService.criaPetPorClienteId(petDTO, usuarioId);

        assertEquals(pet, result);
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void criaPetPorClienteIdSemPermissao() {
        Long usuarioId = 2L;

        usuario.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        petService.criaPetPorClienteId(petDTO, usuarioId);
    }

    @Test(expected = DadoNaoExisteException.class)
    public void criaPetPorClienteIdSemCliente() {
        Long usuarioId = 1L;
        usuario.setPerfil(Perfil.CLIENTE);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        petService.criaPetPorClienteId(petDTO, usuarioId);
    }

    @Test(expected = DadoJaExisteException.class)
    public void criaPetPorClienteIdPetJaExiste() {
        Long usuarioId = 1L;

        cliente.setPets(new ArrayList<>());
        cliente.getPets().add(pet);
        usuario.setPerfil(Perfil.CLIENTE);
        usuario.setCliente(cliente);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        petService.criaPetPorClienteId(petDTO, usuarioId);
    }

    @Test
    public void editaPetPorIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        petDTO.setNome("Novo Pet");

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(petRepository.findById(petDTO.getId())).thenReturn(Optional.of(pet));
        when(racaService.buscaRacaPorId(petDTO.getRaca().getId())).thenReturn(raca);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet result = petService.editaPetPorId(petDTO, usuarioId);

        assertEquals(pet, result);
        assertEquals(petDTO.getNome(), pet.getNome());
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    public void editaPetPorIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;
        petDTO.setNome("Novo Pet");

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        cliente.setUsuario(usuarioProprioSolicitante);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);
        when(petRepository.findById(petDTO.getId())).thenReturn(Optional.of(pet));
        when(racaService.buscaRacaPorId(petDTO.getRaca().getId())).thenReturn(raca);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet result = petService.editaPetPorId(petDTO, usuarioId);

        assertEquals(pet, result);
        assertEquals(petDTO.getNome(), pet.getNome());
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void editaPetPorIdSemPermissao() {
        Long usuarioId = 1L;
        petDTO.setNome("Novo Pet");

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        usuarioProprioSolicitante.setPerfil(Perfil.CLIENTE);
        cliente.setUsuario(usuarioProprioSolicitante);

        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuarioProprioSolicitante);
        when(petRepository.findById(petDTO.getId())).thenReturn(Optional.of(pet));

        petService.editaPetPorId(petDTO, usuarioId);
    }

    @Test
    public void deletaPetPorIdComPermissaoAdmin() {
        Long usuarioId = 1L;
        Long petId = 1L;

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        petService.deletaPetPorId(petId, usuarioId);

        verify(petRepository, times(1)).delete(pet);
    }

    @Test
    public void deletaPetPorIdComPermissaoProprioSolicitante() {
        Long usuarioId = 1L;
        Long petId = 1L;

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        cliente.setUsuario(usuarioProprioSolicitante);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuario);

        petService.deletaPetPorId(petId, usuarioId);

        verify(petRepository, times(1)).delete(pet);
    }

    @Test(expected = UsuarioSemPermissaoException.class)
    public void deletaPetPorIdSemPermissao() {
        Long usuarioId = 1L;
        Long petId = 1L;

        Usuario usuarioProprioSolicitante = new Usuario();
        usuarioProprioSolicitante.setId(2L);
        usuarioProprioSolicitante.setPerfil(Perfil.CLIENTE);
        cliente.setUsuario(usuarioProprioSolicitante);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(usuarioService.buscaUsuario(usuarioId)).thenReturn(usuarioProprioSolicitante);

        petService.deletaPetPorId(petId, usuarioId);
    }
}

