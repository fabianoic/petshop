package br.com.ficampos.petshop.resource;

import br.com.ficampos.petshop.dto.autenticacao.UsuarioDTO;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.resource.swagger.SwaggerUsuarioResource;
import br.com.ficampos.petshop.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/usuario", consumes = {"application/json"}, produces = {"application/json"})
public class UsuarioResource implements SwaggerUsuarioResource {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criaUsuario(@RequestBody @Valid UsuarioDTO usuario,
                                                  @RequestParam(value = "idSolicitante") Long idSolicitante) {
        Usuario novoUsuario = usuarioService.criaUsuario(usuario, idSolicitante);
        UsuarioDTO usuarioDTO = novoUsuario.toDTO();
        return new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(usuarioService.buscaUsuarioPeloCpf(cpf).toDTO());
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorId(@RequestParam Long id) {
        return ResponseEntity.ok(usuarioService.buscaUsuario(id).toDTO());
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> editarUsuarioPorId(@RequestBody @Valid UsuarioDTO usuario,
                                                         @RequestParam Long idSolicitante) {
        return ResponseEntity.ok(usuarioService.editarUsuario(usuario, idSolicitante).toDTO());
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarUsuarioPorId(@RequestParam Long idUsuarioDeletar,
                                                    @RequestParam Long idSolicitante) {
        usuarioService.deletarUsuario(idSolicitante, idUsuarioDeletar);
        return new ResponseEntity(HttpStatus.OK);
    }
}
