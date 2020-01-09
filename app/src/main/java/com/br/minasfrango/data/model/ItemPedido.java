package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.ItemPedidoORM;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ItemPedido implements Serializable {

    ItemPedidoID chavesItemPedido;

    private int bicos;

    private String descricao;

    private long id;

    private double quantidade;

    private double valorTotal;

    private Double valorUnitario;

    private String lote;

    public ItemPedido(ItemPedidoORM itemPedidoORM) {
        this.id = itemPedidoORM.getId();
        this.chavesItemPedido = new ItemPedidoID(itemPedidoORM.getChavesItemPedidoORM());
        this.descricao = itemPedidoORM.getDescricao();
        this.valorUnitario = itemPedidoORM.getValorUnitario();
        this.valorTotal = itemPedidoORM.getValorTotal();
        this.quantidade = itemPedidoORM.getQuantidade();
        this.bicos = itemPedidoORM.getBicos();
        this.lote=itemPedidoORM.getLote();
    }
}
