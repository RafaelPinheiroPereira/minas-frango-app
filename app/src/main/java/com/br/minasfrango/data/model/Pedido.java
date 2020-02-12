package com.br.minasfrango.data.model;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.realm.ItemPedidoORM;
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

    private long idVenda;

    private List<ItemPedido> itens = new ArrayList<>();

    private String motivoCancelamento;

    private long idNucleo;

    private long idEmpresa;
    private double valorTotal;

    private String nomeFoto;

    private boolean fotoMigrada = false;

    public static List<ItemPedido> converterListItemPedidoRealmParaModel(PedidoORM pedidoORM) {
        List<ItemPedido> itens = new ArrayList<>();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            pedidoORM.getItens().forEach(itemPedidoORM -> itens.add(new ItemPedido(itemPedidoORM)));
        } else {
            for (ItemPedidoORM itemPedidoORM : pedidoORM.getItens()) {
                itens.add(new ItemPedido(itemPedidoORM));
            }
        }
        return itens;
    }

    public Pedido(PedidoORM pedidoORM) {
        this.idVenda = pedidoORM.getId();
        this.cancelado = pedidoORM.isCancelado();
        this.codigoCliente = pedidoORM.getCodigoCliente();
        this.codigoFuncionario = pedidoORM.getCodigoFuncionario();
        this.dataPedido = pedidoORM.getDataPedido();
        this.motivoCancelamento = pedidoORM.getMotivoCancelamento();
        this.valorTotal = pedidoORM.getValorTotal();
        this.idEmpresa = pedidoORM.getIdEmpresa();
        this.idNucleo = pedidoORM.getIdNucleo();
        this.nomeFoto = pedidoORM.getNomeFoto();
        this.fotoMigrada = pedidoORM.isFotoMigrada();
    }
}
