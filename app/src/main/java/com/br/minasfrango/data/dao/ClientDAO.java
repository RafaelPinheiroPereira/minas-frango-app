package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.Funcionario;
import com.br.minasfrango.data.realm.Localidade;
import com.br.minasfrango.data.realm.Pedido;
import com.br.minasfrango.data.realm.Rota;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 16/12/2016.
 */
public class ClientDAO extends GenericsDAO<Cliente> {

    public static ClientDAO getInstace(final Class<Cliente> type) {
        return new ClientDAO(type);
    }

    public ClientDAO(final Class<Cliente> type) {
        super(type);
    }

    public List<Cliente> findClientByOrder(List<Pedido> pedidos) {
        List<Cliente> clientes = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            RealmResults<Cliente> clientesResult =
                    where().beginGroup()
                            .equalTo("id", pedido.getCodigoCliente())
                            .endGroup()
                            .findAll();

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

    public List<Cliente> findClientsRoute(Rota rotaToSearch) {
        List<Cliente> clientes = new ArrayList<Cliente>();
        RealmResults<Cliente> results =
                where().equalTo("localidade.rota.id", rotaToSearch.getId())
                        .sort("nome", Sort.DESCENDING)
                        .findAll();
        results.forEach(item->clientes.add(transformaResultEmCliente(item)));
        return clientes;
    }

    public List<Cliente> getAll() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        where().sort("nome")
                .findAll()
                .forEach(item->clientes.add(transformaResultEmCliente(item)));
        return clientes;
    }

    private Cliente transformaResultEmCliente(Cliente clienteToTransforme) {
        Funcionario funcionario =
                new Funcionario(
                        clienteToTransforme.getLocalidade().getRota().getFuncionario().getId(),
                        clienteToTransforme.getLocalidade().getRota().getFuncionario().getSenha(),
                        clienteToTransforme.getLocalidade().getRota().getFuncionario().getNome(),
                        clienteToTransforme
                                .getLocalidade()
                                .getRota()
                                .getFuncionario()
                                .getTipoFuncionario());
        Rota rota =
                new Rota(
                        clienteToTransforme.getLocalidade().getRota().getId(),
                        funcionario,
                        clienteToTransforme.getLocalidade().getRota().getNome());
        Localidade localidade =
                new Localidade(
                        clienteToTransforme.getLocalidade().getId(),
                        clienteToTransforme.getLocalidade().getNome(),
                        rota);
        Cliente cliente =
                new Cliente(
                        clienteToTransforme.getId(),
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
}
