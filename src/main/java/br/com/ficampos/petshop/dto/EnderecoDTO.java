package br.com.ficampos.petshop.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnderecoDTO implements Serializable {

    private Long id;
    @NotNull
    private ClienteDTO cliente;
    @NotNull
    private String logradouro;
    @NotNull
    private String cidade;
    @NotNull
    private String bairro;
    private String complemento;
    private String tag;
}
