package br.com.ficampos.petshop.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO implements Serializable {

    @NotNull
    private ClienteDTO clienteDTO;
    @NotNull
    private RacaDTO racaDTO;
    @NotNull
    private Date dtNascimento;
    @NotNull
    private String nome;
}
