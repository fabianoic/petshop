package br.com.ficampos.petshop.model.autenticacao;

public enum Perfil {
    ADMIN("Admin"), CLIENTE("Cliente");

    private String nome;

    Perfil(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
