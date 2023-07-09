package br.com.ficampos.petshop.repository;

import br.com.ficampos.petshop.model.contato.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    List<Contato> findByClienteId(Long id);
    List<Contato> findByClienteUsuarioId(Long id);
}
