package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Funcionario;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FuncionarioORM extends RealmObject implements Serializable {

    @PrimaryKey
    private long id;

    private String nome;

    private String senha;

    private String tipoFuncionario;

    public FuncionarioORM(Funcionario funcionario) {

        this.id = funcionario.getId();
        this.senha = funcionario.getSenha();
        this.nome = funcionario.getNome();
        this.tipoFuncionario = funcionario.getTipoFuncionario();
    }
}
