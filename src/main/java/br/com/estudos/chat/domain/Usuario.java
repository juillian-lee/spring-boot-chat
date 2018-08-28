package br.com.estudos.chat.domain;

import br.com.estudos.chat.enums.Perfil;

public class Usuario {
    private Long id;
    private String nome;
    private String password;
    private String email;
    private Perfil perfil;
    private boolean active;
}
