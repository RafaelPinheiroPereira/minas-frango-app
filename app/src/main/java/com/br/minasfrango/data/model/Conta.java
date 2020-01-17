package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.ContaORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Conta implements Serializable {
    private String id;
    private String descricao;
    private String agencia;
    private String conta;



    public Conta(ContaORM contaORM) {
        this.id = contaORM.getId();
        this.descricao=contaORM.getDescricao();
        this.agencia=contaORM.getAgencia();
        this.conta = contaORM.getConta();

    }
}
