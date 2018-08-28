package br.com.estudos.chat.domain;

import java.util.List;

public class Veiculo {

    private Long id;
    private String placa;
    private String  modelo;
    private String marca;
    private Integer ano;
    private String chassis;
    private Long km;
    private String cor;
    private List<OrdemServico> ordemServicos;

}
