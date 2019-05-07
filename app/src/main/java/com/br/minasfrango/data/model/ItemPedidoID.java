package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.ItemPedidoIDORM;
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
public class ItemPedidoID implements Serializable {

    private Date dataVenda;

    private long id;

    private double idProduto;

    private String idUnidade;

    private double idVenda;

    private double nucleoCodigo;

    private String tipoVenda;

    private String vendaMae;

    public ItemPedidoID(ItemPedidoIDORM itemPedidoIDORM) {
        this.id = itemPedidoIDORM.getId();
        this.idVenda = itemPedidoIDORM.getIdVenda();
        this.idProduto = itemPedidoIDORM.getIdProduto();
        this.idUnidade = itemPedidoIDORM.getIdUnidade();
        this.dataVenda = itemPedidoIDORM.getDataVenda();
        this.vendaMae = itemPedidoIDORM.getVendaMae();
        this.tipoVenda = itemPedidoIDORM.getTipoVenda();
        this.nucleoCodigo = itemPedidoIDORM.getNucleoCodigo();
    }
}
