package br.com.ficampos.petshop.model;

import br.com.ficampos.petshop.dto.ClienteDTO;
import br.com.ficampos.petshop.dto.autenticacao.ConsultaUsuarioDTO;
import br.com.ficampos.petshop.model.autenticacao.Usuario;
import br.com.ficampos.petshop.model.contato.Contato;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USUARIOID", referencedColumnName = "id")
    private Usuario usuario;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contato> contatos = new ArrayList<>();
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Endereco> enderecos = new ArrayList<>();
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pet> pets;

    @Override
    public Cliente fromDTO(ClienteDTO dto) {
        return null;
    }

    @Override
    public ClienteDTO toDTO() {
        return new ClienteDTO(new ConsultaUsuarioDTO(usuario.getId()),
                enderecos.stream().map(Endereco::toDTO).collect(Collectors.toList()),
                contatos.stream().map(Contato::toDTO).collect(Collectors.toList()),
                pets.stream().map(Pet::toDTO).collect(Collectors.toList()));
    }
}
