package br.com.ficampos.petshop.model;

import br.com.ficampos.petshop.dto.RacaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    private String nome;
    @OneToMany(mappedBy = "raca", fetch = FetchType.LAZY)
    private List<Pet> pet;

    @Override
    public Raca fromDTO(RacaDTO dto) {
        nome = dto.getNome();
        return this;
    }

    @Override
    public RacaDTO toDTO() {
        return new RacaDTO(id, nome);
    }
}
