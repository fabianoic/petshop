package br.com.ficampos.petshop.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContatoDTO implements Serializable {

    @NotNull
    private ClienteDTO clienteDTO;
    @NotNull
    private String tag;
    @NotNull
    private String tipo;
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$|^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")
    @NotNull
    private String valor;

}
