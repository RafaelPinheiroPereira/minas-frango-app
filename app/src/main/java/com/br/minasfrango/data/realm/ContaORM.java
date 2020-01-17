package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.Conta;
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
public class ContaORM  extends RealmObject implements Serializable {


    @PrimaryKey
    private String id;
    private String descricao;
    private String agencia;
    private String conta;

    public ContaORM(Conta conta) {
        this.id = conta.getId();
        this.descricao = conta.getDescricao();
        this.agencia = conta.getAgencia();
        this.conta= conta.getConta();

    }

}
