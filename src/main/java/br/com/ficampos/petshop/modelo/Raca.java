package br.com.ficampos.petshop.modelo;

import br.com.ficampos.petshop.dto.RacaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RACA")
@SequenceGenerator(name = "seqRaca", sequenceName = "SEQRACA", allocationSize = 1)
public class Raca extends EntidadeBase<RacaDTO, Raca> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqRaca")
    private Long id;
    @Column(nullable = false)
    private String descricao;

    @Override
    public Raca fromDTO(RacaDTO dto) {
        return null;
    }
}
