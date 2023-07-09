package br.com.ficampos.petshop.exception;

public class DadoJaExisteException extends RuntimeException {

    private final String mensagem;

    public DadoJaExisteException(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
