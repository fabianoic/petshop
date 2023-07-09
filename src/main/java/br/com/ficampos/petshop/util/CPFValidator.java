package br.com.ficampos.petshop.util;


import br.com.ficampos.petshop.exception.CPFInvalidoException;


public class CPFValidator implements Validator<String> {
    @Override
    public String eValido(String input) {
        try {
            new br.com.caelum.stella.validation.CPFValidator().assertValid(input);
        } catch (Exception e) {
            throw new CPFInvalidoException(e);
        }
        return input;
    }
}
