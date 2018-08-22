package br.com.estudos.chat.repository;

import br.com.estudos.chat.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("FROM Usuario u WHERE u.login = ?1 AND u.password = ?1")
    public Optional<Usuario> login(String login, String password);
}
