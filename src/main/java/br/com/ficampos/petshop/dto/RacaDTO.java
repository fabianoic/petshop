package br.com.ficampos.petshop.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RacaDTO implements Serializable {

    @NotNull
    private String descricao;
}
