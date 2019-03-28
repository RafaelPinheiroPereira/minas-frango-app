package com.br.minasfrango.dao;

import android.service.media.MediaBrowserService.Result;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Pedido;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.internal.IOException;
import java.util.List;
import org.w3c.dom.Entity;

public class PedidoDAO {

    Realm realm;
    RealmQuery<Pedido> pedidoRealmQuery;

    public static PedidoDAO getInstace() {
        return new PedidoDAO();
    }

    public PedidoDAO() {
        realm = Realm.getDefaultInstance();

    }

    public long addPedido(Pedido pedido) {
        long id;
        if (realm.where(Pedido.class).max("id") != null) {
            id = (long) (realm.where(Pedido.class).max("id").intValue() + 1);
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

    public Pedido searchPedido(Cliente cliente) {



        pedidoRealmQuery.equalTo("codigoCliente", cliente.getId());
        RealmResults<Pedido> results = pedidoRealmQuery.findAll();

        if (results.size() > 0 && results != null) {
            results.first();
            return transformaResultEmPedido(results.get(0));

        }
        return null;

    }


    public List<Pedido> buscaPedidosPorCliente(Cliente cliente) {
        List<Pedido> pedidos = new ArrayList<Pedido>();

        pedidoRealmQuery = realm.where(Pedido.class);
        pedidoRealmQuery.equalTo("codigoCliente", cliente.getId());
        RealmResults<Pedido> results = pedidoRealmQuery.findAll();
        if (results.size() > 0 && results != null) {
            for (Pedido auxPedido : results) {
                pedidos.add(transformaResultEmPedido(auxPedido));
            }

        }

        return pedidos;

    }


    public  Pedido findById(long id){
        pedidoRealmQuery=realm.where(Pedido.class);
        Pedido result=pedidoRealmQuery.equalTo("id",id).findAll().first();
        if(result!=null){
            return  transformaResultEmPedido(result);
        }
        return null;
    }


    private Pedido transformaResultEmPedido(Pedido pedidoToTransforme) {
        Pedido pedido = new Pedido();
        pedido.setCodigoFuncionario(pedidoToTransforme.getCodigoFuncionario());
        pedido.setCodigoCliente(pedidoToTransforme.getCodigoCliente());
        pedido.setId(pedidoToTransforme.getId());
        pedido.setValorTotal(pedidoToTransforme.getValorTotal());
        pedido.setTipoRecebimento(pedidoToTransforme.getTipoRecebimento());
        pedido.setDataPedido(pedidoToTransforme.getDataPedido());
        pedido.setMotivoCancelamento(pedidoToTransforme.getMotivoCancelamento());
        pedido.setCancelado(pedidoToTransforme.isCancelado());

        pedido.setItens((RealmList<ItemPedido>) pedidoToTransforme.getItens());
        return pedido;
    }

    public List<Pedido> findAll() {
        List<Pedido> pedidos = new ArrayList<>();
        pedidoRealmQuery = realm.where(Pedido.class);
        RealmResults<Pedido> results=pedidoRealmQuery.findAll();
        if(results.size()>0){
            for(Pedido auxPedido: results)
            pedidos.add(transformaResultEmPedido(auxPedido));
            return pedidos;
        }

        return new ArrayList<Pedido>();
    }

    public ArrayList<Cliente> positivados(String data) {
        RealmQuery<Pedido> queryProduto = realm.where(Pedido.class);
        Date dataFiltro = null;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();

        try {
            dataFiltro = (Date) format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RealmResults<Pedido> results = queryProduto.equalTo("dataPedido", dataFiltro).findAll();
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                Pedido pedido = new Pedido();
                Cliente cliente = new Cliente();
                pedido.setCodigoCliente(results.get(i).getCodigoCliente());
                pedido.setCodigoFuncionario(results.get(i).getCodigoFuncionario());
                cliente.setId(pedido.getCodigoCliente());
                clientes.add(cliente);

            }
        }
        return clientes;
    }

    public ArrayList<Cliente> naoPositivados(String data) {

        RealmQuery<Pedido> queryProduto = realm.where(Pedido.class);
        Date dataFiltro = null;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();

        try {
            dataFiltro = (Date) format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RealmResults<Pedido> results = queryProduto.notEqualTo("dataPedido", dataFiltro).findAll();
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                Pedido pedido = new Pedido();
                Cliente cliente = new Cliente();
                pedido.setCodigoCliente(results.get(i).getCodigoCliente());
                pedido.setCodigoFuncionario(results.get(i).getCodigoFuncionario());
                cliente.setId(pedido.getCodigoCliente());
                clientes.add(cliente);

            }
        }
        return clientes;
    }


    public boolean isPositivado(Cliente cliente, String data) {
        RealmQuery<Pedido> queryProduto = realm.where(Pedido.class);
        Date dataFiltro = null;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();

        try {
            dataFiltro = (Date) format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RealmResults<Pedido> results = queryProduto.equalTo("dataPedido", dataFiltro)
                .equalTo("codigoCliente", cliente.getId()).findAll();

        if (results.size() > 0) {
            return true;
        }
        return false;
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
