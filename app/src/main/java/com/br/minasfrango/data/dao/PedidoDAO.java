package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.PedidoORM;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.internal.IOException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO extends GenericsDAO<PedidoORM> {

    public static PedidoDAO getInstace(final Class<PedidoORM> type) {
        return new PedidoDAO(type);
    }

    public PedidoDAO(final Class<PedidoORM> type) {
        super(type);
    }

    public long addPedido(PedidoORM pedidoORM) {
        long id;
        if (where().max("id") != null) {
            id = (long) (where().max("id").intValue() + 1);
        } else {
            id = 1;
        }
        try {

            pedidoORM.setId(id);

            realm.beginTransaction();
            realm.insert(pedidoORM);
            realm.commitTransaction();

        } catch (RealmException e) {

            realm.cancelTransaction();
        }
        return id;
    }

    public Pedido pesquisarPedido(Cliente clienteParaPesquisar) {

        RealmResults<PedidoORM> results =
                where().equalTo("codigoCliente", clienteParaPesquisar.getId()).findAll();

        if (results.size() > 0 && results != null) {
            results.first();
            return new Pedido(results.get(0));
        }
        return null;
    }

    public List<Pedido> pesquisarPedidosPorCliente(Cliente cliente) {
        List<Pedido> pedidos = new ArrayList<>();
        RealmResults<PedidoORM> results =
                where().equalTo("codigoCliente", cliente.getId()).findAll();
        if (results.size() > 0 && results != null) {
            results.forEach(item->pedidos.add(new Pedido(item)));
        }

        return pedidos;
    }

    public Pedido pesquisarPorId(long id) {

        PedidoORM result = where().equalTo("id", id).findAll().first();
        if (result != null) {
            return new Pedido(result);
        }
        return null;
    }

    public void remove(PedidoORM pedidoORMRealizado) {

        RealmResults<PedidoORM> result =
                realm.where(PedidoORM.class).equalTo("id", pedidoORMRealizado.getId()).findAll();

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

    public List<Pedido> todos() {
        List<Pedido> pedidos = new ArrayList<>();
        RealmResults<PedidoORM> results = where().findAll();
        results.forEach(item->pedidos.add(new Pedido(item)));
        return pedidos;
    }

    public void updatePedido(Pedido pedido) {
        try {

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new PedidoORM(pedido));
            realm.commitTransaction();

        } catch (RealmException ex) {
            realm.cancelTransaction();
        }
    }
}
