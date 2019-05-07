package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.ItemPedidoID;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ItemPedidoIDORM extends RealmObject implements Serializable {

    private Date dataVenda;

    @PrimaryKey
    private long id;

    private double idProduto;

    private String idUnidade;

    private double idVenda;

    private double nucleoCodigo;

    private String tipoVenda;

    private String vendaMae;

    public ItemPedidoIDORM(ItemPedidoID itemPedidoID) {
        this.id = itemPedidoID.getId();
        this.idVenda = itemPedidoID.getIdVenda();
        this.idProduto = itemPedidoID.getIdProduto();
        this.idUnidade = itemPedidoID.getIdUnidade();
        this.dataVenda = itemPedidoID.getDataVenda();
        this.vendaMae = itemPedidoID.getVendaMae();
        this.tipoVenda = itemPedidoID.getTipoVenda();
        this.nucleoCodigo = itemPedidoID.getNucleoCodigo();
    }
}
