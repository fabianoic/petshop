package br.com.ficampos.petshop.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetDTO implements Serializable {

    private Long id;
    @NotNull
    private ClienteDTO cliente;
    @NotNull
    private RacaDTO raca;
    @NotNull
    private Date dtNascimento;
    @NotNull
    private String nome;
}
