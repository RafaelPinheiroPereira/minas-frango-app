package com.br.minasfrango.dao;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Funcionario;
import com.br.minasfrango.model.Localidade;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.model.Rota;

import java.util.ArrayList;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.List;

/**
 * Created by 04717299302 on 16/12/2016.
 */

public class ClienteDAO {

    Realm realm;

    RealmQuery<Cliente> query;


    public static ClienteDAO getInstace() {
        return new ClienteDAO();
    }

    public ClienteDAO() {
        realm = Realm.getDefaultInstance();

    }

    public ArrayList<Cliente> allClientes() {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        RealmResults<Cliente> results = realm.where(Cliente.class).sort("nome", Sort.DESCENDING).findAll();
        if (results != null && results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {

                clientes.add(transformaResultEmCliente(results.get(i)));
            }
        }
        return clientes;
    }

    private Cliente transformaResultEmCliente(Cliente clienteToTransforme) {
        Funcionario funcionario = new Funcionario(
                clienteToTransforme.getLocalidade().getRota().getFuncionario().getId()
                , clienteToTransforme.getLocalidade().getRota().getFuncionario().getSenha(),
                clienteToTransforme.getLocalidade().getRota().getFuncionario().getNome(),
                clienteToTransforme.getLocalidade().getRota().getFuncionario().getTipoFuncionario());
        Rota rota = new Rota(clienteToTransforme.getLocalidade().getRota().getId(), funcionario,
                clienteToTransforme.getLocalidade().getRota().getNome());
        Localidade localidade = new Localidade(clienteToTransforme.getLocalidade().getId(),
                clienteToTransforme.getLocalidade().getNome(), rota);
        Cliente cliente = new Cliente(clienteToTransforme.getId(),
                clienteToTransforme.getNome(),
                clienteToTransforme.getRazaoSocial(),
                localidade,
                clienteToTransforme.getEndereco(),
                clienteToTransforme.getNumero(),
                clienteToTransforme.getBairro(),
                clienteToTransforme.getCidade(),
                clienteToTransforme.getCep(),
                clienteToTransforme.getReferencia(),
                clienteToTransforme.getTelefone(),
                clienteToTransforme.getCpf());
        return cliente;
    }

    public List<Cliente> carregaClienteByPedidos(List<Pedido> pedidos){
        List<Cliente>clientes= new ArrayList<>();
        for(Pedido pedido:pedidos){
            query = realm.where(Cliente.class);

            RealmResults<Cliente> cliente=query.beginGroup()
                    .equalTo("id", pedido.getCodigoCliente()).
                    endGroup().findAll();

            Cliente aux =new Cliente();
            if(cliente.size()>0) {
                aux= transformaResultEmCliente(cliente.first());
            }
            if(!clientes.contains(aux)){
                clientes.add(aux);
            }
        }

        return clientes;
    }

    public ArrayList<Cliente> carregaClientesPorRota(Rota rotaToSearch) {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        RealmQuery<Cliente> clienteRealmQuery = realm.where(Cliente.class);
        RealmResults<Cliente> results = clienteRealmQuery.equalTo("localidade.rota.id", rotaToSearch.getId())
                .sort("nome", Sort.DESCENDING).findAll();
        if (results != null) {
            for (int i = 0; i < results.size(); i++) {

                clientes.add(transformaResultEmCliente(results.get(i)));
            }
        }
        return clientes;

    }

    public Cliente findById(Long id) {
        query = realm.where(Cliente.class);
        Cliente cliente = query.equalTo("id", id).findFirst();
        if (cliente != null) {
            return transformaResultEmCliente(cliente);

        } else {
            return null;
        }
    }


}
