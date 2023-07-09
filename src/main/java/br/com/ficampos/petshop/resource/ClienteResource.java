package br.com.ficampos.petshop.resource;

import br.com.ficampos.petshop.dto.ClienteDTO;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.resource.swagger.SwaggerClienteResource;
import br.com.ficampos.petshop.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cliente", consumes = {"application/json"}, produces = {"application/json"})
public class ClienteResource implements SwaggerClienteResource {

    @Autowired
    private ClienteService clienteService;

    @Override
    @GetMapping
    public ResponseEntity<ClienteDTO> buscaClientePorId(@RequestParam Long clienteId) {
        Cliente cliente = clienteService.buscaClientePorId(clienteId);
        return cliente == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(cliente.toDTO());
    }
}

