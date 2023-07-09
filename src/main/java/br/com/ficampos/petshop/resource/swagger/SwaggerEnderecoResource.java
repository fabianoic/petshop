package br.com.ficampos.petshop.resource.swagger;

import br.com.ficampos.petshop.dto.EnderecoDTO;
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

@Api(tags = "Endereco")
public interface SwaggerEnderecoResource {

    String retornoCriaEnderecoStatus201 = "";
    String retornoCriaEnderecoStatus200 = "";

    @ApiOperation(value = "Cria endereco por cliente ID", notes = "Retorna um endereco com ID se persistido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno de sucesso na criação do endereco", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaEnderecoStatus201)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", description = "Retorno sem permissão(ADMIN ou Proprietário)"),
            @ApiResponse(responseCode = "400", description = "Retorno para erro na criação")})
    ResponseEntity<EnderecoDTO> criaEnderecoPorClienteId(@RequestBody EnderecoDTO enderecoDTO, @RequestParam Long usuarioId);

    @ApiOperation(value = "Busca enderecos por cliente ID", notes = "Retorna lista de enderecos do cliente do ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta dos enderecos", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaEnderecoStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Retorna status de não encontrado")})
    ResponseEntity<List<EnderecoDTO>> buscaEnderecoPorClienteId(@RequestParam Long clienteId);

    @ApiOperation(value = "Busca enderecos por usuario ID", notes = "Retorna lista de enderecos do usuario do ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso na consulta dos enderecos", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaEnderecoStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<EnderecoDTO>> buscaEnderecoPorClienteUsuarioId(@PathVariable Long usuarioId);

    @ApiOperation(value = "Edita endereco pelo ID", notes = "Retorna o endereco alterado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao editar endereco", content = {
                    @Content(examples = {
                            @ExampleObject(value = retornoCriaEnderecoStatus200)}, mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    public ResponseEntity<EnderecoDTO> editaEnderecoPorId(@RequestBody EnderecoDTO enderecoDTO, @RequestParam Long usuarioId);

    @ApiOperation(value = "Deleta endereco pelo ID", notes = "Deleta endereco pelo ID setado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de sucesso ao deletar endereco")})
    public ResponseEntity<Void> deletaEnderecoPorId(@RequestParam Long id, @RequestParam Long usuarioId);
}
