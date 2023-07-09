package br.com.ficampos.petshop.resource.swagger;

import br.com.ficampos.petshop.dto.PetDTO;
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

@Api(tags = "Pet")
public interface SwaggerPetResource {

    String retornoCriaPetStatus201 = "";
    String retornoCriaPetStatus200 = "";

    @ApiOperation(value = "Cria pet por cliente ID", notes = "Retorna um pet com ID se persistido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno de sucesso na criação do pet", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaPetStatus201)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", description = "Retorno sem permissão(ADMIN ou Proprietário)"),
            @ApiResponse(responseCode = "400", description = "Retorno para erro na criação")})
    ResponseEntity<PetDTO> criaPetPorClienteId(@RequestBody PetDTO PetDTO, @RequestParam Long usuarioId);

    @ApiOperation(value = "Busca pets por cliente ID", notes = "Retorna lista de pets do cliente do ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta dos pets", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaPetStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Retorna status de não encontrado")})
    ResponseEntity<List<PetDTO>> buscaPetPorClienteId(@RequestParam Long clienteId);

    @ApiOperation(value = "Busca Pets por usuario ID", notes = "Retorna lista de Pets do usuario do ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta dos Pets", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaPetStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<PetDTO>> buscaPetPorClienteUsuarioId(@PathVariable Long usuarioId);

    @ApiOperation(value = "Edita Pet pelo ID", notes = "Retorna o Pet alterado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao editar Pet", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaPetStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    public ResponseEntity<PetDTO> editaPetPorId(@RequestBody PetDTO PetDTO, @RequestParam Long usuarioId);

    @ApiOperation(value = "Deleta Pet pelo ID", notes = "Deleta Pet pelo ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao deletar Pet")})
    public ResponseEntity<Void> deletaPetPorId(@RequestParam Long id, @RequestParam Long usuarioId);
}
