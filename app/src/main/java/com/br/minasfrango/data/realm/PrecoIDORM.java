package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.PrecoID;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PrecoIDORM extends RealmObject implements Serializable {

    private Date dataPreco;

    private double idCliente;

    private long idProduto;

    private String unidadeProduto;

    @PrimaryKey
    private String id;

    public PrecoIDORM(PrecoID precoID) {

        this.id = precoID.getId() + "-" + precoID.getIdProduto() + "-" + precoID.getUnidadeProduto() + "-" + precoID
                .getIdCliente() + "-" + precoID.getDataPreco();
        this.idProduto = precoID.getIdProduto();
        this.unidadeProduto = precoID.getUnidadeProduto();
        this.idCliente = precoID.getIdCliente();
        this.dataPreco = precoID.getDataPreco();
    }
}
