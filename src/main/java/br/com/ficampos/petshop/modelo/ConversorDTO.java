package br.com.ficampos.petshop.modelo;

import java.io.Serializable;

public interface ConversorDTO<T, O> extends Serializable {

    O fromDTO(T dto);
}
