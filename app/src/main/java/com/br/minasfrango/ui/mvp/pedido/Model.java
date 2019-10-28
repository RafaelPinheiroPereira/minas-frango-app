package com.br.minasfrango.ui.mvp.pedido;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.ClientePedido;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.ui.mvp.pedido.IPedidoMVP.IModel;
import java.util.ArrayList;
import java.util.List;

public class Model implements IModel {

    private ClienteDAO mClienteDAO = ClienteDAO.getInstace(ClienteORM.class);

    private PedidoDAO mPedidoDAO = PedidoDAO.getInstace(PedidoORM.class);

    private Presenter mPresenter;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void cancelarPedido(String motivoCancelamento) {
        mPresenter.getPedido().setCancelado(true);
        mPresenter.getPedido().setMotivoCancelamento(motivoCancelamento);
        mPedidoDAO.updatePedido(mPresenter.getPedido());
    }

    @Override
    public List<ClientePedido> getAllClientePedidos() {
        List<ClientePedido> clientePedidos = new ArrayList<>();
        List<Cliente> clientes = pesquisarClientePorPedido(todosPedidos());

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            clientes.forEach(
                    cliente->{
                        List<Pedido> pedidos;
                        pedidos = mPedidoDAO.pesquisarPedidosPorCliente(cliente);
                        if (pedidos.size() > 0) {
                        }
                        clientePedidos.add(new ClientePedido(cliente, pedidos));
                    });
        } else {
            for (Cliente cliente : clientes) {
                List<Pedido> pedidos;
                pedidos = mPedidoDAO.pesquisarPedidosPorCliente(cliente);
                if (pedidos.size() > 0) {
                }
                clientePedidos.add(new ClientePedido(cliente, pedidos));
            }
        }

        return clientePedidos;
    }

    @Override
    public List<Cliente> pesquisarClientePorPedido(final List<Pedido> pedido) {
        return mClienteDAO.pesquisarClientePorPedido(pedido);
    }

    @Override
    public List<Pedido> todosPedidos() {
        return mPedidoDAO.todos();
    }
}
