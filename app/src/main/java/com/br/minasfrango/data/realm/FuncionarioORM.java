package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Funcionario;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
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
public class FuncionarioORM extends RealmObject implements Serializable {

    @PrimaryKey private long id;

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

    private boolean sincronizou=false;

    public FuncionarioORM(Funcionario funcionario) {

        this.id = funcionario.getId();
        this.senha = funcionario.getSenha();
        this.nome = funcionario.getNome();
        this.tipoFuncionario = funcionario.getTipoFuncionario();
        this.idEmpresa = funcionario.getIdEmpresa();
        this.maxIdVenda = funcionario.getMaxIdVenda();
        this.maxIdRecibo = funcionario.getMaxIdRecibo();
        this.idPastaFuncionario = funcionario.getIdPastaFuncionario();
        this.idPastaPagamentos = funcionario.getIdPastaPagamentos();
        this.idPastaVendas = funcionario.getIdPastaVendas();
        this.alteraPreco=funcionario.getAlteraPreco();
        this.dataUltimaSincronizacao=funcionario.getDataUltimaSincronizacao();
    }
}
