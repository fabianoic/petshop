package br.com.ficampos.petshop.model;

import br.com.ficampos.petshop.dto.PetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PET")
@SequenceGenerator(name = "seqPet", sequenceName = "SEQPET", allocationSize = 1)
public class Pet extends EntidadeBase<PetDTO, Pet> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqPet")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "CLIENTEID", referencedColumnName = "ID")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "RACAID", referencedColumnName = "ID")
    private Raca raca;
    @Temporal(value = TemporalType.DATE)
    private Date dtNascimento;
    @Column(nullable = false)
    private String nome;

    @Override
    public Pet fromDTO(PetDTO dto) {
        dtNascimento = dto.getDtNascimento();
        nome = dto.getNome();
        return this;
    }

    @Override
    public PetDTO toDTO() {
        return new PetDTO(id, null, raca.toDTO(), dtNascimento, nome);
    }
}
