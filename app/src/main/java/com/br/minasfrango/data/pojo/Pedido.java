package com.br.minasfrango.data.pojo;

import com.br.minasfrango.data.dao.ItemPedidoDAO;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
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
public class Pedido extends RealmObject implements Serializable {


    @PrimaryKey
    private long id;

    private long codigoCliente;

    private long codigoFuncionario;
    private RealmList<ItemPedido> itens;
    @Ignore
    private List<ItemPedido> mItemPedidos;
    private double valorTotal;

    private long tipoRecebimento;

    private Date dataPedido;

    private String motivoCancelamento;

    private boolean cancelado;
    
    public List<ItemPedido> realmListToList() {
        ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstace(ItemPedido.class);
        List<ItemPedido> itemPedidos = new ArrayList<ItemPedido>();
        getItens().forEach(item->itemPedidos.add(itemPedidoDAO.searchItem(item)));
        return itemPedidos;
    }

    public static RealmList<ItemPedido> dtoToRealList(List<ItemPedido> itemPedidos) {
        RealmList<ItemPedido> realmList = new RealmList<>();
        itemPedidos.forEach(item->realmList.add(item));
        return realmList;
    }

}
