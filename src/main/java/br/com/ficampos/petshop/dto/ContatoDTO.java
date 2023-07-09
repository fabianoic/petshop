package br.com.ficampos.petshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContatoDTO implements Serializable {

    private Long id;
    @NotNull
    private ClienteDTO cliente;
    @NotNull
    private String tag;
    @NotNull
    private String tipo;
    @Pattern(regexp = "^(\\\\d{11}|[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,})$")
    @NotNull
    private String valor;

}
