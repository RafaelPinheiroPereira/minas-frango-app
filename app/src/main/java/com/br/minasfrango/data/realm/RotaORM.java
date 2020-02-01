package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Rota;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RotaORM extends RealmObject implements Serializable {

    @PrimaryKey
    @ToString.Exclude
    private long id;

    @ToString.Exclude
    private FuncionarioORM mFuncionarioORM;

    private String nome;

    public RotaORM(Rota rota) {
        this.id = rota.getId();
        //this.mFuncionarioORM = new FuncionarioORM(rota.getFuncionario());
        this.nome = rota.getNome();
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
