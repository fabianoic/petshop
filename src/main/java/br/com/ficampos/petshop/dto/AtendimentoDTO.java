package br.com.ficampos.petshop.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtendimentoDTO implements Serializable {

    @NotNull
    private PetDTO petDTO;
    @NotNull
    private String descricao;
    @NotNull
    private BigDecimal valor;
    private Date data;

}
