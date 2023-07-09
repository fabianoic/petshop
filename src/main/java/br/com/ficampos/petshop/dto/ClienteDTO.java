package br.com.ficampos.petshop.dto;

import br.com.ficampos.petshop.dto.autenticacao.ConsultaUsuarioDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteDTO implements Serializable {

    @NotNull
    private ConsultaUsuarioDTO usuario;
    private List<EnderecoDTO> enderecos;
    private List<ContatoDTO> contatos;
    private List<PetDTO> pets;


}
