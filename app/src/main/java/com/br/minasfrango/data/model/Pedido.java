package com.br.minasfrango.data.model;

import com.br.minasfrango.data.dao.ItemPedidoDAO;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido extends RealmObject implements Serializable {


    @PrimaryKey
    private long id;

    private long codigoCliente;

    private long codigoFuncionario;

    private RealmList<ItemPedido> itens;

    private double valorTotal;

    private long tipoRecebimento;

    private Date dataPedido;

    private String motivoCancelamento;

    private boolean cancelado;
    
    public List<ItemPedido> realmListToList() {
        ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstace();
        List<ItemPedido> itemPedidos = new ArrayList<ItemPedido>();
        for (ItemPedido aux : getItens()) {

            itemPedidos.add(itemPedidoDAO.searchItem(aux));
        }
        return itemPedidos;
    }

}
