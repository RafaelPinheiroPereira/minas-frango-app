package com.br.minasfrango.ui.mvp.pedido;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.ClientePedido;
import com.br.minasfrango.data.realm.Pedido;
import com.br.minasfrango.ui.mvp.pedido.IPedidoMVP.IModel;
import java.util.ArrayList;
import java.util.List;

public class Model implements IModel {

    private ClientDAO mClientDAO = ClientDAO.getInstace(Cliente.class);

    private PedidoDAO mPedidoDAO = PedidoDAO.getInstace(Pedido.class);

    private Presenter mPresenter;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public List<Pedido> allPedidos() {
        return mPedidoDAO.findAll();
    }

    @Override
    public void cancelOrder(String cancelingMotive) {
        mPresenter.getPedido().setCancelado(true);
        mPresenter.getPedido().setMotivoCancelamento(cancelingMotive);
        mPedidoDAO.updatePedido(mPresenter.getPedido());
    }

    @Override
    public List<Cliente> findClientByPedidos(final List<Pedido> pedidos) {
        return mClientDAO.findClientByOrder(pedidos);
    }

    @Override
    public List<ClientePedido> getAllClientePedidos() {
        List<ClientePedido> clientePedidos = new ArrayList<>();
        List<Cliente> clientes = findClientByPedidos(allPedidos());

        clientes.forEach(cliente->{

            List<Pedido> pedidos;
            pedidos = mPedidoDAO.pesquisarPedidosPorCliente(cliente);
            if (pedidos.size() > 0) {
            }
            clientePedidos.add(new ClientePedido(cliente, pedidos));

        });

        return clientePedidos;
    }


}
