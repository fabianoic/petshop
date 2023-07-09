package br.com.ficampos.petshop.resource;

import br.com.ficampos.petshop.dto.EnderecoDTO;
import br.com.ficampos.petshop.model.Endereco;
import br.com.ficampos.petshop.resource.swagger.SwaggerEnderecoResource;
import br.com.ficampos.petshop.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/endereco", consumes = {"application/json"}, produces = {"application/json"})
public class EnderecoResource implements SwaggerEnderecoResource {

    @Autowired
    private EnderecoService enderecoService;

    @Override
    @PostMapping
    public ResponseEntity<EnderecoDTO> criaEnderecoPorClienteId(@RequestBody EnderecoDTO enderecoDTO, @RequestParam Long usuarioId) {
        return new ResponseEntity(enderecoService.criaEnderecoPorUsuarioId(enderecoDTO, usuarioId).toDTO(), HttpStatus.CREATED);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> buscaEnderecoPorClienteId(@RequestParam Long clienteId) {
        List<Endereco> enderecos = enderecoService.buscaEnderecosPorClienteId(clienteId);
        List<EnderecoDTO> enderecoDTOs = enderecos.stream().map(Endereco::toDTO).collect(Collectors.toList());
        return enderecoDTOs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(enderecoDTOs);
    }

    @Override
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<EnderecoDTO>> buscaEnderecoPorClienteUsuarioId(@PathVariable Long usuarioId) {
        List<Endereco> enderecos = enderecoService.buscaEnderecosPorClienteUsuarioId(usuarioId);
        List<EnderecoDTO> enderecoDTOs = enderecos.stream().map(Endereco::toDTO).collect(Collectors.toList());
        return enderecoDTOs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(enderecoDTOs);
    }

    @Override
    @PutMapping
    public ResponseEntity<EnderecoDTO> editaEnderecoPorId(@RequestBody EnderecoDTO enderecoDTO, @RequestParam Long usuarioId) {
        Endereco endereco = enderecoService.editaEnderecoPorId(enderecoDTO, usuarioId);
        return ResponseEntity.ok(endereco.toDTO());
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deletaEnderecoPorId(@RequestParam Long id, @RequestParam Long usuarioId) {
        enderecoService.deletaEnderecoPorId(id, usuarioId);
        return ResponseEntity.ok().build();
    }
}

