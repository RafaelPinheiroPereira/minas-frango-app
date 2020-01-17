package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.PrecoORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Preco implements Serializable {

    PrecoID chavesPreco;
    private String id;
    private double valor;
    private long idEmpresa;
    private long idNucleo;

    public Preco(PrecoORM precoORM) {
        this.id = precoORM.getId();
        this.chavesPreco = new PrecoID(precoORM.getChavesPrecoORM());
        this.valor = precoORM.getValor();
        this.idEmpresa=precoORM.getIdEmpresa();
        this.idNucleo=precoORM.getIdNucleo();
    }
}
