package br.com.ficampos.petshop.resource;

import br.com.ficampos.petshop.dto.RacaDTO;
import br.com.ficampos.petshop.model.Raca;
import br.com.ficampos.petshop.resource.swagger.SwaggerRacaResource;
import br.com.ficampos.petshop.service.RacaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/raca", consumes = {"application/json"}, produces = {"application/json"})
public class RacaResource implements SwaggerRacaResource {

    @Autowired
    private RacaService racaService;

    @PostMapping
    public ResponseEntity<RacaDTO> criaRacaPorClienteId(@RequestBody RacaDTO racaDTO, @RequestParam Long usuarioId) {
        return new ResponseEntity(racaService.criaRaca(racaDTO, usuarioId).toDTO(), HttpStatus.CREATED);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RacaDTO>> buscaRacas() {
        List<Raca> racas = racaService.buscaRacas();
        List<RacaDTO> racaDTOs = racas.stream().map(Raca::toDTO).collect(Collectors.toList());
        return racaDTOs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(racaDTOs);
    }

    @GetMapping("/{racaId}")
    public ResponseEntity<RacaDTO> buscaRacaPorClienteUsuarioId(@PathVariable Long racaId) {
        Raca raca = racaService.buscaRacaPorId(racaId);
        return raca == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(raca.toDTO());
    }

    @PutMapping
    public ResponseEntity<RacaDTO> editaRacaPorId(@RequestBody RacaDTO racaDTO, @RequestParam Long usuarioId) {
        Raca endereco = racaService.editaRacaPorId(racaDTO, usuarioId);
        return ResponseEntity.ok(endereco.toDTO());
    }

    @DeleteMapping
    public ResponseEntity<Void> deletaRacaPorId(@RequestParam Long id, @RequestParam Long usuarioId) {
        racaService.deletaRacaPorId(id, usuarioId);
        return ResponseEntity.ok().build();
    }
}

