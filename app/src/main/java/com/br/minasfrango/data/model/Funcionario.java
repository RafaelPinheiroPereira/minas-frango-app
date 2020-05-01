package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.FuncionarioORM;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Funcionario implements Serializable {

    private long id;

    private String nome;

    private String senha;

    private String tipoFuncionario;

    private long idEmpresa;

    private long maxIdVenda;

    private long maxIdRecibo;

    private String idPastaFuncionario;

    private String idPastaVendas;

    private String idPastaPagamentos;

    private String alteraPreco;

    private Date dataUltimaSincronizacao;



    public Funcionario(FuncionarioORM funcionarioORM) {
        this.id = funcionarioORM.getId();
        this.senha = funcionarioORM.getSenha();
        this.nome = funcionarioORM.getNome();
        this.tipoFuncionario = funcionarioORM.getTipoFuncionario();
        this.idEmpresa = funcionarioORM.getIdEmpresa();
        this.maxIdVenda = funcionarioORM.getMaxIdVenda();
        this.maxIdRecibo = funcionarioORM.getMaxIdRecibo();
        this.idPastaFuncionario = funcionarioORM.getIdPastaFuncionario();
        this.idPastaPagamentos = funcionarioORM.getIdPastaPagamentos();
        this.idPastaVendas = funcionarioORM.getIdPastaVendas();
        this.alteraPreco=funcionarioORM.getAlteraPreco();
        this.dataUltimaSincronizacao=funcionarioORM.getDataUltimaSincronizacao();

    }
}
