package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.ItemPedido;
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
public class ItemPedidoORM extends RealmObject implements Serializable {

    ItemPedidoIDORM chavesItemPedidoORM;

    private int bicos;

    private String descricao;

    @PrimaryKey
    private long id;

    private double quantidade;

    private double valorTotal;

    private Double valorUnitario;

    private String lote;

    public ItemPedidoORM(ItemPedido itemPedido) {
        this.id = itemPedido.getId();
        this.chavesItemPedidoORM = new ItemPedidoIDORM(itemPedido.getChavesItemPedido());
        this.descricao = itemPedido.getDescricao();
        this.valorUnitario = itemPedido.getValorUnitario();
        this.valorTotal = itemPedido.getValorTotal();
        this.quantidade = itemPedido.getQuantidade();
        this.bicos = itemPedido.getBicos();
        this.lote=itemPedido.getLote();
    }
}
