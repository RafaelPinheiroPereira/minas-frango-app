package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.PrecoIDORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PrecoID implements Serializable {

    private long id;

    private double idCliente;

    private long idProduto;

    private String unidadeProduto;

    public PrecoID(PrecoIDORM precoIDORM) {

        this.id = precoIDORM.getId();
        this.idProduto = precoIDORM.getIdProduto();
        this.unidadeProduto = precoIDORM.getUnidadeProduto();
        this.idCliente = precoIDORM.getIdCliente();
    }
}
