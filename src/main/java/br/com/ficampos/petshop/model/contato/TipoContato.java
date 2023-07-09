package br.com.ficampos.petshop.model.contato;

public enum TipoContato {

    EMAIL("E-mail"), TELEFONE("Telefone");

    private String tipo;

    TipoContato(String tipo) {
        this.tipo = tipo;
    }
}
