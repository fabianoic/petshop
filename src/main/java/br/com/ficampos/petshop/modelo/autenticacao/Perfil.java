package br.com.ficampos.petshop.modelo.autenticacao;

public enum Perfil {
    ADMIN("Admin"), CLIENTE("Cliente");

    private String nome;

    Perfil(String nome) {
        this.nome = nome;
    }
}
