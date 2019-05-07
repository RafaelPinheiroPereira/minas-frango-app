package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.PrecoID;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PrecoIDORM extends RealmObject implements Serializable {

    @PrimaryKey
    private long id;

    private double idCliente;

    private long idProduto;

    private String unidadeProduto;

    public PrecoIDORM(PrecoID precoID) {

        this.id = precoID.getId();
        this.idProduto = precoID.getIdProduto();
        this.unidadeProduto = precoID.getUnidadeProduto();
        this.idCliente = precoID.getIdCliente();
    }
}
