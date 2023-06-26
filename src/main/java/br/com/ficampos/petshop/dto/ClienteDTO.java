package br.com.ficampos.petshop.dto;

import br.com.ficampos.petshop.dto.autenticacao.UsuarioDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO implements Serializable {

    @NotNull
    private UsuarioDTO usuarioDTO;
}
