package br.com.ficampos.petshop.resource;

import br.com.ficampos.petshop.dto.PetDTO;
import br.com.ficampos.petshop.model.Pet;
import br.com.ficampos.petshop.resource.swagger.SwaggerPetResource;
import br.com.ficampos.petshop.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/pet", consumes = {"application/json"}, produces = {"application/json"})
public class PetResource implements SwaggerPetResource {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<PetDTO> criaPetPorClienteId(@RequestBody PetDTO PetDTO, @RequestParam Long usuarioId) {
        return new ResponseEntity(petService.criaPetPorClienteId(PetDTO, usuarioId).toDTO(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PetDTO>> buscaPetPorClienteId(@RequestParam Long clienteId) {
        List<Pet> pets = petService.buscaPetsPorClienteId(clienteId);
        List<PetDTO> PetDTOs = pets.stream().map(Pet::toDTO).collect(Collectors.toList());
        return PetDTOs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(PetDTOs);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<PetDTO>> buscaPetPorClienteUsuarioId(@PathVariable Long usuarioId) {
        List<Pet> pets = petService.buscaPetsPorClienteUsuarioId(usuarioId);
        List<PetDTO> PetDTOs = pets.stream().map(Pet::toDTO).collect(Collectors.toList());
        return PetDTOs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(PetDTOs);
    }

    @PutMapping
    public ResponseEntity<PetDTO> editaPetPorId(@RequestBody PetDTO PetDTO, @RequestParam Long usuarioId) {
        Pet pet = petService.editaPetPorId(PetDTO, usuarioId);
        return ResponseEntity.ok(pet.toDTO());
    }

    @DeleteMapping
    public ResponseEntity<Void> deletaPetPorId(@RequestParam Long id, @RequestParam Long usuarioId) {
        petService.deletaPetPorId(id, usuarioId);
        return ResponseEntity.ok().build();
    }
}

