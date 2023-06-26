package br.com.ficampos.petshop.modelo.contato;

public enum TipoContato {

    EMAIL("E-mail"), TELEFONE("Telefone");

    private String tipo;

    TipoContato(String tipo) {
        this.tipo = tipo;
    }
}
