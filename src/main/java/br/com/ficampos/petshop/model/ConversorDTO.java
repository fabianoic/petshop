package br.com.ficampos.petshop.model;

import java.io.Serializable;

public interface ConversorDTO<T, O> extends Serializable {

    O fromDTO(T dto);

    T toDTO();
}
