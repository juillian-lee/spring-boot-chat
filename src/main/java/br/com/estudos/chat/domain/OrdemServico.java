package br.com.estudos.chat.domain;

import br.com.estudos.chat.enums.StatusOrdemServico;

import java.util.List;

public class OrdemServico {
    private Long id;
    private String numero;
    private String file;
    private StatusOrdemServico status;
    private String observacoes;
    private List<Servico> servicos;

}
