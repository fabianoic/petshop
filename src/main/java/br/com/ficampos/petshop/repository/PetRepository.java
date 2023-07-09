package br.com.ficampos.petshop.repository;

import br.com.ficampos.petshop.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByClienteId(Long id);
    List<Pet> findByClienteUsuarioId(Long id);
}
