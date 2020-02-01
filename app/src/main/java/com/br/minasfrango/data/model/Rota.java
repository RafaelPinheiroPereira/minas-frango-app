package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.RotaORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rota implements Serializable {

    private Funcionario funcionario;

    private long id;

    private String nome;

    public Rota(RotaORM rotaORM) {
        this.id = rotaORM.getId();
        //this.funcionario = new Funcionario(rotaORM.getMFuncionarioORM());
        this.nome = rotaORM.getNome();
    }

    @Override
    public String toString() {
        return this.getNome();
    }

}
