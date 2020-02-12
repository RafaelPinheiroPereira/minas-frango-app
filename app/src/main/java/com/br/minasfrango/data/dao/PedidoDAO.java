package com.br.minasfrango.data.dao;

import static com.br.minasfrango.data.model.Pedido.converterListItemPedidoRealmParaModel;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.PedidoORM;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.internal.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoDAO extends GenericsDAO<PedidoORM> {

    public static PedidoDAO getInstace(final Class<PedidoORM> type) {
        return new PedidoDAO(type);
    }

    public PedidoDAO(final Class<PedidoORM> type) {
        super(type);
    }

    public Pedido consultarPedidoPorNomeDaFoto(final String name) {
        return new Pedido(where().equalTo("nomeFoto",name).findFirst());
    }

    public List<Pedido> getPedidosNaoMigrados() {

        List<Pedido> pedidos = new ArrayList<>();
        RealmResults<PedidoORM> results =
                where().equalTo("fotoMigrada", false).findAll();
        if (results.size() > 0 && results != null) {
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                results.forEach(pedidoORM->pedidos.add(new Pedido(pedidoORM)));
            } else {
                for (PedidoORM pedidoORM : results) {
                    pedidos.add(new Pedido(pedidoORM));
                }
            }
        }

        return pedidos;

    }

    public void salvarPedido(PedidoORM pedidoORM) {


            realm.beginTransaction();
            realm.copyToRealmOrUpdate(pedidoORM);
            realm.commitTransaction();


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
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                results.forEach(pedidoORM->pedidos.add(new Pedido(pedidoORM)));
            } else {
                for (PedidoORM pedidoORM : results) {
                    pedidos.add(new Pedido(pedidoORM));
                }
            }
        }

        return pedidos;
    }


    public List<Pedido> pesquisarPedidosPorPeriodo(Date dataInicial,Date dataFinal) throws ParseException {
        List<Pedido> pedidos = new ArrayList<>();




        RealmResults<PedidoORM> results =
                where()
                       // .between("dataPedido",dataInicial,dataFinal)
                        .findAll();
        if (results.size() > 0 && results != null) {
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                results.forEach(pedidoORM->pedidos.add(new Pedido(pedidoORM)));
            } else {
                for (PedidoORM pedidoORM : results) {
                    pedidos.add(new Pedido(pedidoORM));
                }
            }
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
        if (VERSION.SDK_INT >= VERSION_CODES.N) {

            results.forEach(pedidoORM->{
                Pedido pedido =  new Pedido(pedidoORM) ;
                List<ItemPedido> itemPedidos=converterListItemPedidoRealmParaModel(pedidoORM);
                pedido.setItens(itemPedidos);
                pedidos.add(pedido);});
        } else {
            for (PedidoORM pedidoORM : results) {
                Pedido pedido =  new Pedido(pedidoORM) ;
                List<ItemPedido> itemPedidos=converterListItemPedidoRealmParaModel(pedidoORM);
                pedido.setItens(itemPedidos);
                pedidos.add(pedido);
            }
        }
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

    public boolean existePedidoComIdMaiorDoQueOCodigoDeVendaMaxima(long codigoVendaMaxima){
        long id;
        if (where().max("id") != null) {
            id = where().max("id").longValue();
            return id > codigoVendaMaxima;
            }
        else {
           return false;
        }
    }
}
