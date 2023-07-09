package br.com.ficampos.petshop.resource;

import br.com.ficampos.petshop.dto.ContatoDTO;
import br.com.ficampos.petshop.model.contato.Contato;
import br.com.ficampos.petshop.resource.swagger.SwaggerContatoResource;
import br.com.ficampos.petshop.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/contato", consumes = {"application/json"}, produces = {"application/json"})
public class ContatoResource implements SwaggerContatoResource {

    @Autowired
    private ContatoService contatoService;

    @Override
    @PostMapping
    public ResponseEntity<ContatoDTO> criaContatoPorClienteId(@RequestBody ContatoDTO contatoDTO, @RequestParam Long usuarioId) {
        return new ResponseEntity(contatoService.criaContatoPorClienteId(contatoDTO, usuarioId).toDTO(), HttpStatus.CREATED);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ContatoDTO>> buscaContatoPorClienteId(@RequestParam Long clienteId) {
        List<Contato> contatos = contatoService.buscaContatosPorClienteId(clienteId);
        List<ContatoDTO> contatoDTOs = contatos.stream().map(Contato::toDTO).collect(Collectors.toList());
        return contatoDTOs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(contatoDTOs);
    }

    @Override
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<ContatoDTO>> buscaContatoPorClienteUsuarioId(@PathVariable Long usuarioId) {
        List<Contato> contatos = contatoService.buscaContatosPorClienteUsuarioId(usuarioId);
        List<ContatoDTO> contatoDTOs = contatos.stream().map(Contato::toDTO).collect(Collectors.toList());
        return contatoDTOs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(contatoDTOs);
    }

    @Override
    @PutMapping
    public ResponseEntity<ContatoDTO> editaContatoPorId(@RequestBody ContatoDTO contatoDTO, @RequestParam Long usuarioId) {
        Contato contato = contatoService.editaContatoPorId(contatoDTO, usuarioId);
        return ResponseEntity.ok(contato.toDTO());
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deletaContatoPorId(@RequestParam Long id, @RequestParam Long usuarioId) {
        contatoService.deletaContatoPorId(id, usuarioId);
        return ResponseEntity.ok().build();
    }
}

