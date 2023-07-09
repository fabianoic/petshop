package br.com.ficampos.petshop.dto.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaUsuarioDTO implements Serializable {

    private Long id;
}
