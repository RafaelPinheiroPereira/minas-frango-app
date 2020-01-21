package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.FuncionarioORM;
import java.io.Serializable;
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

    private  long maxIdVenda;



    public Funcionario(FuncionarioORM funcionarioORM) {
        this.id = funcionarioORM.getId();
        this.senha = funcionarioORM.getSenha();
        this.nome = funcionarioORM.getNome();
        this.tipoFuncionario = funcionarioORM.getTipoFuncionario();
        this.idEmpresa=funcionarioORM.getIdEmpresa();
        this.maxIdVenda=funcionarioORM.getMaxIdVenda();


    }
}
