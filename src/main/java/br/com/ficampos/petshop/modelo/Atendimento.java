package br.com.ficampos.petshop.modelo;

import br.com.ficampos.petshop.dto.AtendimentoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ATENDIMENTO")
@SequenceGenerator(name = "seqAtendimento", sequenceName = "SEQATENDIMENTO", allocationSize = 1)
public class Atendimento extends EntidadeBase<AtendimentoDTO, Atendimento> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqAtendimento")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PETID", referencedColumnName = "ID")
    private Pet pet;
    @Column(nullable = false)
    private String descricao;
    @Column(scale = 2)
    private BigDecimal valor;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date data;

    @Override
    public Atendimento fromDTO(AtendimentoDTO dto) {
        return null;
    }
}
