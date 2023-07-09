package br.com.ficampos.petshop.repository;

import br.com.ficampos.petshop.model.Raca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacaRepository extends JpaRepository<Raca, Long> {
}
