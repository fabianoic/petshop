package br.com.ficampos.petshop.modelo;

import br.com.ficampos.petshop.dto.EnderecoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ENDERECO")
@SequenceGenerator(name = "seqEndereco", sequenceName = "SEQENDERECO", allocationSize = 1)
public class Endereco extends EntidadeBase<EnderecoDTO, Endereco> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqEndereco")
    private Long id;
    @OneToOne
    @JoinColumn(name = "CLIENTEID", referencedColumnName = "ID")
    private Cliente cliente;
    @Column(nullable = false)
    private String logradouro;
    @Column(nullable = false)
    private String cidade;
    @Column(nullable = false)
    private String bairro;
    @Column
    private String complemento;
    @Column
    private String tag;





    @Override
    public Endereco fromDTO(EnderecoDTO dto) {
        return null;
    }
}
