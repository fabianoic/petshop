package br.com.ficampos.petshop.util;

import br.com.ficampos.petshop.dto.ClienteDTO;

public class PermissaoUtil {

    public static boolean validaUsuario(ClienteDTO cliente, Long usuarioId) {
        return  cliente.getUsuario() != null &&
                cliente.getUsuario().getId() != null &&
                cliente.getUsuario().getId().equals(usuarioId);
    }
}
