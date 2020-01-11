package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.ClienteORM;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 16/12/2016.
 */
public class ClienteDAO extends GenericsDAO<ClienteORM> {

    public static ClienteDAO getInstace(final Class<ClienteORM> type) {
        return new ClienteDAO(type);
    }

    public ClienteDAO(final Class<ClienteORM> type) {
        super(type);
    }

    public List<Cliente> pesquisarClientePorPedido(List<Pedido> pedidos) {
        List<Cliente> clientes = new ArrayList<>();

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            pedidos.forEach(
                    pedido->{
                        ClienteORM clienteORM =
                                where().beginGroup()
                                        .equalTo("id", pedido.getCodigoCliente())
                                        .endGroup()
                                        .findAll()
                                        .first();
                        if (clienteORM != null && !clientes.contains(new Cliente(clienteORM))) {
                            clientes.add(new Cliente(clienteORM));
                        }
                    });
        } else {
            for (Pedido pedido : pedidos) {
                ClienteORM clienteORM =
                        where().beginGroup()
                                .equalTo("id", pedido.getCodigoCliente())
                                .endGroup()
                                .findAll()
                                .first();
                if (clienteORM != null && !clientes.contains(new Cliente(clienteORM))) {
                    clientes.add(new Cliente(clienteORM));
                }
            }
        }

        return clientes;
    }

    public List<Cliente> pesquisarClientePorRede(ClienteGrupo clienteGrupo) {
        List<Cliente> clientes = new ArrayList<>();
        RealmResults<ClienteORM> results =
                where().equalTo("codigoClienteGrupo", clienteGrupo.getId())
                        .sort("nome", Sort.DESCENDING)
                        .findAll();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            results.forEach(item->clientes.add(new Cliente(item)));
        } else {
            for (ClienteORM item : results) {
                clientes.add(new Cliente(item));
            }
        }
        return clientes;
    }

    public List<Cliente> todos() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        RealmResults<ClienteORM> results = where().sort("nome",Sort.ASCENDING).findAll();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            results.forEach(item->clientes.add(new Cliente(item)));
        } else {
            for (ClienteORM item : results) {
                clientes.add(new Cliente(item));
            }
        }
        return clientes;
    }
}
