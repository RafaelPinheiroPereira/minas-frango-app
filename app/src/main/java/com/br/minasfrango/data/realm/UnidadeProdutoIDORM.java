package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.UnidadeProdutoID;
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
public class UnidadeProdutoIDORM extends RealmObject implements Serializable {

    @PrimaryKey
    private long idProduto;

    private String idUnidade;

    public UnidadeProdutoIDORM(UnidadeProdutoID unidadeProdutoID) {
        this.idProduto = unidadeProdutoID.getIdProduto();
        this.idUnidade = unidadeProdutoID.getIdUnidade();
    }
}
