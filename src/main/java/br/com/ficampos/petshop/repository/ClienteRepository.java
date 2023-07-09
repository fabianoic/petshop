package br.com.ficampos.petshop.repository;

import br.com.ficampos.petshop.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByUsuarioCpf(String cpf);
}
