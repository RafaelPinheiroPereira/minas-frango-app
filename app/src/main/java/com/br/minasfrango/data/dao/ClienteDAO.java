package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Localidade;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Rota;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 16/12/2016.
 */

public class ClienteDAO extends DAO<Cliente> {


    public static ClienteDAO getInstace() {
        return new ClienteDAO();
    }

    public ClienteDAO() {
        super();
    }

    private RealmQuery<Cliente> where() {
        return realm.where(Cliente.class);
    }

    public ArrayList<Cliente> allClientes() {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        RealmResults<Cliente> results = where().sort("nome", Sort.DESCENDING).findAll();
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

    public List<Cliente> pesquisarClientePorPedido(List<Pedido> pedidos) {
        List<Cliente> clientes = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            RealmResults<Cliente> clientesResult = where().beginGroup()
                    .equalTo("id", pedido.getCodigoCliente()).
                            endGroup().findAll();

            Cliente aux = new Cliente();
            if (clientesResult.size() > 0) {
                aux = transformaResultEmCliente(clientesResult.first());
            }
            if (!clientes.contains(aux)) {
                clientes.add(aux);
            }
        }

        return clientes;
    }

    public ArrayList<Cliente> pesquisarClientePorRota(Rota rotaToSearch) {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        RealmResults<Cliente> results = where().equalTo("localidade.rota.id", rotaToSearch.getId())
                .sort("nome", Sort.DESCENDING).findAll();
        if (results != null) {
            for (int i = 0; i < results.size(); i++) {
                clientes.add(transformaResultEmCliente(results.get(i)));
            }
        }
        return clientes;

    }

    public Cliente findById(Long id) {
        return where().equalTo("id", id).findFirst();
    }
}
