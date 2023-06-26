package br.com.ficampos.petshop.modelo;

import br.com.ficampos.petshop.dto.ClienteDTO;
import br.com.ficampos.petshop.modelo.autenticacao.Usuario;
import br.com.ficampos.petshop.modelo.contato.Contato;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENTE")
@SequenceGenerator(name = "seqCliente", sequenceName = "SEQCLIENTE", allocationSize = 1)
public class Cliente extends EntidadeBase<ClienteDTO, Cliente> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqCliente")
    private Long id;
    @OneToOne
    @JoinColumn(name = "USUARIOID", referencedColumnName = "CPF")
    private Usuario usuario;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contato> contatos;

    @Override
    public Cliente fromDTO(ClienteDTO dto) {
        return null;
    }
}
