package com.br.minasfrango.data.realm;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
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

    private long idEmpresa;

    private RealmList<ItemPedidoORM> itens;

    private String motivoCancelamento;


    private double valorTotal;
    private long idNucleo;

    private String nomeFoto;

    private boolean fotoMigrada = false;

    public static RealmList<ItemPedidoORM> converterListModelParaListRealm(
            List<ItemPedido> itens) {
        RealmList<ItemPedidoORM> realmList = new RealmList<>();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            itens.forEach(item->realmList.add(new ItemPedidoORM(item)));
        } else {
            for (ItemPedido itemPedido : itens) {
                realmList.add(new ItemPedidoORM(itemPedido));
            }
        }
        return realmList;
    }

    public static List<ItemPedido> converterListRealmParaModel(
            List<ItemPedidoORM> itemPedidoORMS) {
        List<ItemPedido> itemPedidos = new RealmList<>();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            itemPedidoORMS.forEach(itemORM->itemPedidos.add(new ItemPedido(itemORM)));
        } else {
            for (ItemPedidoORM itemPedidoORM : itemPedidoORMS) {
                itemPedidos.add(new ItemPedido(itemPedidoORM));
            }
        }
        return itemPedidos;
    }

    public PedidoORM(Pedido pedido) {
        this.id = pedido.getIdVenda();
        this.cancelado = pedido.isCancelado();
        this.codigoCliente = pedido.getCodigoCliente();
        this.codigoFuncionario = pedido.getCodigoFuncionario();
        this.dataPedido = pedido.getDataPedido();
        this.motivoCancelamento = pedido.getMotivoCancelamento();
        this.valorTotal = pedido.getValorTotal();
        this.idEmpresa=pedido.getIdEmpresa();
        this.idNucleo=pedido.getIdNucleo();
        this.nomeFoto=pedido.getNomeFoto();
        this.fotoMigrada =pedido.isFotoMigrada();
    }
}
