package br.com.ficampos.petshop.model.autenticacao;

import br.com.ficampos.petshop.dto.autenticacao.UsuarioDTO;
import br.com.ficampos.petshop.model.Cliente;
import br.com.ficampos.petshop.model.EntidadeBase;
import br.com.ficampos.petshop.util.CPFValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USUARIO")
@SequenceGenerator(name = "seqUsuario", sequenceName = "SEQUSUARIO", allocationSize = 1)
public class Usuario extends EntidadeBase<UsuarioDTO, Usuario> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqUsuario")
    private Long id;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false)
    private String nome;
    @Enumerated(value = EnumType.STRING)
    private Perfil perfil;
    @Column(nullable = false)
    private String senha;
    @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Cliente cliente;

    @Override
    public Usuario fromDTO(UsuarioDTO dto) {
        cpf = new CPFValidator().eValido(dto.getCpf());
        nome = dto.getNome();
        perfil = dto.getPerfil();
        senha = dto.getSenha();
        return this;
    }

    @Override
    public UsuarioDTO toDTO() {
        return new UsuarioDTO(id, null, nome, perfil, null);
    }
}
