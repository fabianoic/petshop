package br.com.ficampos.petshop.repository;

import br.com.ficampos.petshop.model.autenticacao.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCpf(String cpf);

    boolean existsUsuarioByCpf(String cpf);
}
