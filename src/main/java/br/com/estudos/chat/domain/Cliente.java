package br.com.estudos.chat.domain;

import java.util.Date;
import java.util.List;

public class Cliente {

    private Long id;
    private String nome;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String contato;
    private String telefone;
    private String celular;

    private Date dateUpdated;
    private Date dateCreated;

    private List<Veiculo> veiculos;
}
