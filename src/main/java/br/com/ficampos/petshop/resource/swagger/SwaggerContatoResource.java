package br.com.ficampos.petshop.resource.swagger;

import br.com.ficampos.petshop.dto.ContatoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "Contato")
public interface SwaggerContatoResource {

    String retornoCriaContatoStatus201 = "{\"id\":1,\"tag\":\"pessoal\",\"tipo\":\"EMAIL\",\"valor\":\"fabiano.fic@gmail.com\"}";
    String retornoCriaContatoStatus200 = "[{\"id\":1,\"tag\":\"pessoal\",\"tipo\":\"EMAIL\",\"valor\":\"fabiano.fic@gmail.com\"},{\"id\":2,\"tag\":\"trabalho\",\"tipo\":\"TELEFONE\",\"valor\":\"53981389666\"}]";

    @ApiOperation(value = "Cria contato por cliente ID", notes = "Retorna um contato com ID se persistido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno de sucesso na criação do contato", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaContatoStatus201)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", description = "Retorno sem permissão(ADMIN ou Proprietário)"),
            @ApiResponse(responseCode = "400", description = "Retorno para erro na criação")})
    ResponseEntity<ContatoDTO> criaContatoPorClienteId(@RequestBody ContatoDTO contatoDTO, @RequestParam Long usuarioId);

    @ApiOperation(value = "Busca contatos por cliente ID", notes = "Retorna lista de contatos do cliente do ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta dos contatos", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaContatoStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Retorna status de não encontrado")})
    ResponseEntity<List<ContatoDTO>> buscaContatoPorClienteId(@RequestParam Long clienteId);

    @ApiOperation(value = "Busca contatos por usuario ID", notes = "Retorna lista de contatos do usuario do ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta dos contatos", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaContatoStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Retorna status de não encontrado")})
    ResponseEntity<List<ContatoDTO>> buscaContatoPorClienteUsuarioId(@PathVariable Long usuarioId);

    @ApiOperation(value = "Edita contato pelo ID", notes = "Retorna o contato alterado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao editar contato", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaContatoStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", description = "Retorno sem permissão(ADMIN ou Proprietário)")})
    ResponseEntity<ContatoDTO> editaContatoPorId(@RequestBody ContatoDTO contatoDTO, @RequestParam Long usuarioId);

    @ApiOperation(value = "Deleta contato pelo ID", notes = "Deleta contato pelo ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao deletar contato"),
            @ApiResponse(responseCode = "403", description = "Retorno sem permissão(ADMIN ou Proprietário)")})
    ResponseEntity<Void> deletaContatoPorId(@RequestParam Long id, @RequestParam Long usuarioId);
}
