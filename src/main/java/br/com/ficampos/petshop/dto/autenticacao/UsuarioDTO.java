package br.com.ficampos.petshop.dto.autenticacao;

import br.com.ficampos.petshop.model.autenticacao.Perfil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO implements Serializable {

    private Long id;
    @NotNull
    @NotEmpty
    private String nome;
    @NotNull
    @NotEmpty
//    @ApiModelProperty(example = "Número do CPF ou do CPJ completo e sem pontuações")
    private String cpf;
    @NotNull
    private Perfil perfil;
    private String senha;
}
