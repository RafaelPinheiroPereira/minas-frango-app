package com.br.minasfrango.data.realm;

import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.Pedido;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PedidoORM extends RealmObject implements Serializable {

    private boolean cancelado;

    private long codigoCliente;

    private long codigoFuncionario;

    private Date dataPedido;

    @PrimaryKey
    private long id;

    private RealmList<ItemPedidoORM> itens;

    private String motivoCancelamento;

    private long tipoRecebimento;

    private double valorTotal;

    public static RealmList<ItemPedidoORM> converterListModelParaListRealm(
            List<ItemPedido> itemPedido) {
        RealmList<ItemPedidoORM> realmList = new RealmList<>();
        itemPedido.forEach(item->realmList.add(new ItemPedidoORM(item)));
        return realmList;
    }

    public PedidoORM(Pedido pedido) {
        this.id = pedido.getId();
        this.cancelado = pedido.isCancelado();
        this.codigoCliente = pedido.getCodigoCliente();
        this.codigoFuncionario = pedido.getCodigoFuncionario();
        this.dataPedido = pedido.getDataPedido();
        this.motivoCancelamento = pedido.getMotivoCancelamento();
        this.tipoRecebimento = pedido.getTipoRecebimento();
        this.valorTotal = pedido.getValorTotal();
    }
}
