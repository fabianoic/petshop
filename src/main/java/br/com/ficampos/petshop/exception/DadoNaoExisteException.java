package br.com.ficampos.petshop.exception;

public class DadoNaoExisteException extends RuntimeException {

    private final String mensagem;

    public DadoNaoExisteException(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
