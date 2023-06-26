package br.com.ficampos.petshop.modelo.contato;

import br.com.ficampos.petshop.dto.ContatoDTO;
import br.com.ficampos.petshop.modelo.Cliente;
import br.com.ficampos.petshop.modelo.EntidadeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONTATO")
@SequenceGenerator(name = "seqContato", sequenceName = "SEQCONTATO", allocationSize = 1)
public class Contato extends EntidadeBase<ContatoDTO, Contato> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqContato")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "CLIENTEID", referencedColumnName = "ID")
    private Cliente cliente;
    @Column(nullable = false)
    private String tag;
    @Enumerated(value = EnumType.STRING)
    private TipoContato tipo;
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$|^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")
    @Column(nullable = false)
    private String valor;



    @Override
    public Contato fromDTO(ContatoDTO dto) {
        return null;
    }
}
