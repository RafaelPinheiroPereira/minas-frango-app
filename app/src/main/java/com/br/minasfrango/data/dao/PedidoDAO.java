package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.Pedido;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.internal.IOException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO extends GenericsDAO<Pedido> {


    public static PedidoDAO getInstace(final Class<Pedido> type) {
        return new PedidoDAO(type);
    }

    public PedidoDAO(final Class<Pedido> type) {
        super(type);

    }

    public long addPedido(Pedido pedido) {
        long id;
        if (where().max("id") != null) {
            id = (long) (where().max("id").intValue() + 1);
        } else {
            id = 1;
        }
        try {

            pedido.setId(id);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(pedido);
            realm.commitTransaction();

        } catch (RealmException e) {

            realm.cancelTransaction();

        }
        return id;
    }

    public List<Pedido> findAll() {
        List<Pedido> pedidos = new ArrayList<>();
        RealmResults<Pedido> results = where().findAll();
        results.forEach(item->pedidos.add(convertRealmToDTO(item)));
        return pedidos;
    }

    public Pedido findById(long id) {

        Pedido result = where().equalTo("id", id).findAll().first();
        if (result != null) {
            return convertRealmToDTO(result);
        }
        return null;
    }

    public Pedido pesquisarPedido(Cliente cliente) {

        RealmResults<Pedido> results = where().equalTo("codigoCliente", cliente.getId()).findAll();

        if (results.size() > 0 && results != null) {
            results.first();
            return convertRealmToDTO(results.get(0));

        }
        return null;

    }

    public List<Pedido> pesquisarPedidosPorCliente(Cliente cliente) {
        List<Pedido> pedidos = new ArrayList<Pedido>();
        RealmResults<Pedido> results = where().equalTo("codigoCliente", cliente.getId()).findAll();
        if (results.size() > 0 && results != null) {
            for (Pedido auxPedido : results) {
                pedidos.add(convertRealmToDTO(auxPedido));
            }

        }

        return pedidos;

    }

    private Pedido convertRealmToDTO(Pedido pedidoToTransforme) {
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

    public void updatePedido(Pedido pedido) {
        try {

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(pedido);
            realm.commitTransaction();

        } catch (RealmException ex) {
            realm.cancelTransaction();
        }

    }

    public void remove(Pedido pedidoRealizado) {

        RealmResults<Pedido> result = realm.where(Pedido.class).equalTo("id", pedidoRealizado.getId()).findAll();

        if (result.size() > 0 && result != null) {
            try {

                realm.beginTransaction();
                result.deleteAllFromRealm();
                realm.commitTransaction();

            } catch (IOException e) {
                realm.cancelTransaction();
            }
        }
    }
}
