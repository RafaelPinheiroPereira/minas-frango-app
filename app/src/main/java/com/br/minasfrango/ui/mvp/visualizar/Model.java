package com.br.minasfrango.ui.mvp.visualizar;

import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.ItemPedidoORM;
import com.br.minasfrango.data.realm.PedidoORM;

public class Model implements IViewOrderMVP.IModel {

    ClienteDAO mClienteDAO = ClienteDAO.getInstace(ClienteORM.class);

    PedidoDAO mPedidoDAO = PedidoDAO.getInstace(PedidoORM.class);

    ItemPedidoDAO mItemPedidoDAO = ItemPedidoDAO.getInstace(ItemPedidoORM.class);



    IViewOrderMVP.IPresenter mPresenter;




    public Model(final Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void atualizarPedido(final Pedido pedido) {
        this.mPedidoDAO.alterar(new PedidoORM(pedido));
    }

    @Override
    public Cliente pesquisarClientePorID(final long codigoCliente) {
        return new Cliente(this.mClienteDAO.findById(codigoCliente));
    }



    @Override
    public Pedido pesquisarVendaPorId(final Long id) {


        PedidoORM pedidoORM = mPedidoDAO.findById(id);
        Pedido pedido = new Pedido(pedidoORM);
        pedido.setItens(PedidoORM.converterListRealmParaModel(pedidoORM.getItens()));
        return pedido;


    }
}
