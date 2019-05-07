package com.br.minasfrango.data.model;

import com.br.minasfrango.data.realm.PedidoORM;
import java.io.Serializable;
import java.util.ArrayList;
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
public class Pedido implements Serializable {

    private boolean cancelado;

    private long codigoCliente;

    private long codigoFuncionario;

    private Date dataPedido;

    private long id;

    private List<ItemPedido> itens = new ArrayList<>();

    private String motivoCancelamento;

    private long tipoRecebimento;

    private double valorTotal;

    public static List<ItemPedido> converterListItemPedidoRealmParaModel(PedidoORM pedidoORM) {
        List<ItemPedido> itens = new ArrayList<>();
        pedidoORM.getItens().forEach(itemPedidoORM->itens.add(new ItemPedido(itemPedidoORM)));
        return itens;
    }

    public Pedido(PedidoORM pedidoORM) {
        this.id = pedidoORM.getId();
        this.cancelado = pedidoORM.isCancelado();
        this.codigoCliente = pedidoORM.getCodigoCliente();
        this.codigoFuncionario = pedidoORM.getCodigoFuncionario();
        this.dataPedido = pedidoORM.getDataPedido();
        this.motivoCancelamento = pedidoORM.getMotivoCancelamento();
        this.tipoRecebimento = pedidoORM.getTipoRecebimento();
        this.valorTotal = pedidoORM.getValorTotal();
    }
}
