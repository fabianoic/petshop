package br.com.ficampos.petshop.modelo.autenticacao;

import br.com.ficampos.petshop.dto.autenticacao.UsuarioDTO;
import br.com.ficampos.petshop.modelo.EntidadeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USUARIO")
public class Usuario extends EntidadeBase<UsuarioDTO, Usuario> {

    @Id
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false)
    private String nome;
    @Enumerated(value = EnumType.STRING)
    private Perfil perfil;
    @Column(nullable = false)
    private String senha;



    @Override
    public Usuario fromDTO(UsuarioDTO dto) {
        return null;
    }
}
