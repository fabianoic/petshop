package br.com.ficampos.petshop.repository;

import br.com.ficampos.petshop.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    List<Endereco> findByClienteId(Long id);
    List<Endereco> findByClienteUsuarioId(Long id);
}
