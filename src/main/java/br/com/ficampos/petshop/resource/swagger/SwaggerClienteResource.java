package br.com.ficampos.petshop.resource.swagger;

import br.com.ficampos.petshop.dto.ClienteDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "Cliente")
public interface SwaggerClienteResource {

    String retornoCriaClienteStatus200 = "{\"usuario\":{\"id\":2},\"enderecos\":[{\"id\":1,\"logradouro\":\"Rua José Martelli, 361\",\"cidade\":\"Bento Gonçalves\",\"bairro\":\"Maria Goretti\",\"complemento\":\"apto 507\",\"tag\":\"Casa\"}],\"contatos\":[{\"id\":1,\"tag\":\"pessoal\",\"tipo\":\"EMAIL\",\"valor\":\"fabiano.fic@gmail.com\"},{\"id\":2,\"tag\":\"trabalho\",\"tipo\":\"TELEFONE\",\"valor\":\"53981389666\"}],\"pets\":[{\"id\":1,\"raca\":{\"id\":1,\"nome\":\"Rottweiler\"},\"dtNascimento\":1688526000000,\"nome\":\"Bob\"}]}";

    @Operation(summary = "buscaClientePorId", description = "Retorno de Sucesso na consulta do Cliente", parameters = {
            @Parameter(in = ParameterIn.QUERY, name = "clienteId", description = "ID do cliente a ser consultado", required = true, example = "1", schema = @Schema(type = "Long"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaClienteStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Não Encontrado")})
    ResponseEntity<ClienteDTO> buscaClientePorId(@RequestParam Long clienteId);
}
