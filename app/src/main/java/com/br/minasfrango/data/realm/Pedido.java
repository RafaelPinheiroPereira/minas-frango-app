package com.br.minasfrango.data.realm;

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

    private boolean cancelado;

    private long codigoCliente;

    private long codigoFuncionario;

    private Date dataPedido;

    @PrimaryKey
    private long id;

    private RealmList<ItemPedido> itens;

    @Ignore
    private List<ItemPedido> mItemPedidos;

    private String motivoCancelamento;

    private long tipoRecebimento;

    private double valorTotal;

    /**
     * Tenho que implementar todas as conversoes de Realm para DTO e vice - versa
     *
     * @param pedidoToTransforme do tipo pedido realm
     * @return um pedido do tipo dto
     */
    public static Pedido convertRealmToDTO(Pedido pedidoToTransforme) {
        Pedido pedido = new Pedido();
        pedido.setCodigoFuncionario(pedidoToTransforme.getCodigoFuncionario());
        pedido.setCodigoCliente(pedidoToTransforme.getCodigoCliente());
        pedido.setId(pedidoToTransforme.getId());
        pedido.setValorTotal(pedidoToTransforme.getValorTotal());
        pedido.setTipoRecebimento(pedidoToTransforme.getTipoRecebimento());
        pedido.setDataPedido(pedidoToTransforme.getDataPedido());
        pedido.setMotivoCancelamento(pedidoToTransforme.getMotivoCancelamento());
        pedido.setCancelado(pedidoToTransforme.isCancelado());
        pedido.setItens(pedidoToTransforme.getItens());
        pedido.setMItemPedidos(pedidoToTransforme.realmListToDTO());
        return pedido;

    }

    /**
     * Convert to Object  DTO for Object RealmList
     */
    public static RealmList<ItemPedido> dtoToRealList(List<ItemPedido> itemPedidos) {
        RealmList<ItemPedido> realmList = new RealmList<>();
        itemPedidos.forEach(item->realmList.add(item));
        return realmList;
    }

    /**
     * @return List DTOs OBJECTs
     */
    public List<ItemPedido> realmListToDTO() {
        ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstace(ItemPedido.class);
        List<ItemPedido> itemPedidos = new ArrayList<>();
        getItens().forEach(item->itemPedidos.add(itemPedidoDAO.searchItem(item)));
        return itemPedidos;
    }
}
