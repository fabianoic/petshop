package br.com.ficampos.petshop.resource.swagger;

import br.com.ficampos.petshop.dto.RacaDTO;
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

@Api(tags = "Raca")
public interface SwaggerRacaResource {

    String retornoCriaRacaStatus201 = "";
    String retornoCriaRacaStatus200 = "";

    @ApiOperation(value = "Cria Raca por cliente ID", notes = "Retorna um Raca com ID se persistido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno de sucesso na criação do Raca", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaRacaStatus201)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", description = "Retorno sem permissão(ADMIN ou Proprietário)"),
            @ApiResponse(responseCode = "400", description = "Retorno para erro na criação")})
    ResponseEntity<RacaDTO> criaRacaPorClienteId(@RequestBody RacaDTO RacaDTO, @RequestParam Long usuarioId);

    @ApiOperation(value = "Busca Racas por cliente ID", notes = "Retorna lista de Racas do cliente do ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta dos Racas", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaRacaStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Retorna status de não encontrado")})
    ResponseEntity<List<RacaDTO>> buscaRacas();

    @ApiOperation(value = "Busca Racas por raca ID", notes = "Retorna lista de Racas do raca do ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta dos Racas", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaRacaStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    ResponseEntity<RacaDTO> buscaRacaPorClienteUsuarioId(@PathVariable Long racaId);

    @ApiOperation(value = "Edita Raca pelo ID", notes = "Retorna o Raca alterado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao editar Raca", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaRacaStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    ResponseEntity<RacaDTO> editaRacaPorId(@RequestBody RacaDTO RacaDTO, @RequestParam Long usuarioId);

    @ApiOperation(value = "Deleta Raca pelo ID", notes = "Deleta Raca pelo ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao deletar Raca")})
    ResponseEntity<Void> deletaRacaPorId(@RequestParam Long id, @RequestParam Long usuarioId);
}
