package br.com.ficampos.petshop.resource.swagger;

import br.com.ficampos.petshop.dto.autenticacao.UsuarioDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Api(tags = "Usuario")
public interface SwaggerUsuarioResource {

    String retornoCriaUsuarioStatus201 = "{    \"nome\": \"Fabiano Campos\", \"cpf\": \"123456789-10\", \"perfil\": \"ADMIN\" }";
    String retornoCriaUsuarioStatus200 = "{    \"nome\": \"Fabiano Campos\", \"cpf\": \"123456789-10\", \"perfil\": \"ADMIN\" }";
    String retornoCriaUsuarioStatus400 = "{ \"mensagem\": \"Usuario com CPF 123456789-10 ja cadastrado\"}";


    @Operation(summary = "criaUsuario", parameters = {
            @Parameter(in = ParameterIn.QUERY, name = "idSolicitante", description = "ID do usuario a solicitante", required = true, example = "1", schema = @Schema(type = "Long"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno de sucesso na criação do Usuário", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaUsuarioStatus201)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Retorno de bad request na criação de Usuário", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaUsuarioStatus400)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    ResponseEntity<UsuarioDTO> criaUsuario(@RequestBody @Valid UsuarioDTO usuario,
                                           @RequestParam(value = "idSolicitante") Long idSolicitante);

    @Operation(summary = "buscaUsuarioPorCpf", parameters = {
            @Parameter(in = ParameterIn.QUERY, name = "cpf", description = "CPF do usuário a ser consultado", required = true, example = "46386919009", schema = @Schema(type = "String"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta do Usuário", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaUsuarioStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Retorno de not found para usuário não encontrado")})
    ResponseEntity<UsuarioDTO> buscaUsuarioPorCpf(@PathVariable String cpf);

    @Operation(summary = "buscaUsuarioPorId", parameters = {
            @Parameter(in = ParameterIn.QUERY, name = "id", description = "ID do usuário a ser consultado", required = true, example = "1", schema = @Schema(type = "Long"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta do Usuário", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaUsuarioStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Retorno de not found para usuário não encontrado")})
    ResponseEntity<UsuarioDTO> buscaUsuarioPorId(@RequestParam Long id);

    @Operation(summary = "editarUsuarioPorId", parameters = {
            @Parameter(in = ParameterIn.QUERY, name = "idSolicitante", description = "ID do usuário a ser consultado", required = true, example = "1", schema = @Schema(type = "Long"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao editar o Usuário", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaUsuarioStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    ResponseEntity<UsuarioDTO> editarUsuarioPorId(@RequestBody @Valid UsuarioDTO usuario,
                                                  @RequestParam Long idSolicitante);
    @Operation(summary = "deletarUsuarioPorId", parameters = {
            @Parameter(in = ParameterIn.QUERY, name = "idSolicitante", description = "ID do usuário a ser consultado", required = true, example = "1", schema = @Schema(type = "Long")),
            @Parameter(in = ParameterIn.QUERY, name = "idUsuarioDeletar", description = "ID do usuário a ser deletado", required = true, example = "2", schema = @Schema(type = "Long"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao deletar Usuário", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaUsuarioStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    ResponseEntity<Void> deletarUsuarioPorId(@RequestParam Long idUsuarioDeletar,
                                             @RequestParam Long idSolicitante);
}
