package br.com.ficampos.petshop.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO implements Serializable {

    @NotNull
    private ClienteDTO clienteDTO;
    @NotNull
    private String logradouro;
    @NotNull
    private String cidade;
    @NotNull
    private String bairro;
    private String complemento;
    private String tag;
}
