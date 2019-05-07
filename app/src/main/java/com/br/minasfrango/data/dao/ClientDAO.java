package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Rota;
import com.br.minasfrango.data.realm.ClienteORM;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 16/12/2016.
 */
public class ClientDAO extends GenericsDAO<ClienteORM> {

    public static ClientDAO getInstace(final Class<ClienteORM> type) {
        return new ClientDAO(type);
    }

    public ClientDAO(final Class<ClienteORM> type) {
        super(type);
    }

    public List<Cliente> pesquisarClientePorPedido(List<Pedido> pedidos) {
        List<Cliente> clientes = new ArrayList<>();
        pedidos.forEach(
                pedido->{
                    ClienteORM clientesResult =
                            where().beginGroup()
                                    .equalTo("id", pedido.getCodigoCliente())
                                    .endGroup()
                                    .findAll()
                                    .first();
                    if (clientesResult != null && !clientes.contains(new Cliente(clientesResult))) {
                        clientes.add(new Cliente(clientesResult));
                    }
                });

        return clientes;
    }

    public List<Cliente> pesquisarClientePorRota(Rota rotaParaPesquisar) {
        List<Cliente> clientes = new ArrayList<>();
        RealmResults<ClienteORM> results =
                where().equalTo("localidadeORM.rotaORM.id", rotaParaPesquisar.getId())
                        .sort("nome", Sort.DESCENDING)
                        .findAll();
        results.forEach(item->clientes.add(new Cliente(item)));
        return clientes;
    }

    public List<Cliente> todos() {
        ArrayList<Cliente> cliente = new ArrayList<>();
        where().sort("nome").findAll().forEach(item->cliente.add(new Cliente(item)));
        return cliente;
    }


}
