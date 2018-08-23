package br.com.estudos.chat.service;

import br.com.estudos.chat.entity.Usuario;
import br.com.estudos.chat.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> login(String login, String password) {
        return usuarioRepository.login(login, password);
    }

}
