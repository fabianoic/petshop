package br.com.ficampos.petshop.dto.autenticacao;

import com.sun.istack.NotNull;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {

    @NotNull
    private String nome;
    @NotNull
    private String perfil;
}
