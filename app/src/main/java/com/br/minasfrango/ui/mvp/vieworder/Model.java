package com.br.minasfrango.ui.mvp.vieworder;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.ItemPedido;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.data.pojo.TipoRecebimento;

public class Model implements IViewOrderMVP.IModel {

    ClientDAO mClientDAO = ClientDAO.getInstace(Cliente.class);

    ItemPedidoDAO mItemPedidoDAO = ItemPedidoDAO.getInstace(ItemPedido.class);

    PedidoDAO mPedidoDAO = PedidoDAO.getInstace(Pedido.class);

    IViewOrderMVP.IPresenter mPresenter;

    TipoRecebimentoDAO mTipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();

    public Model(final Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public Cliente findClientByID(final long codigoCliente) {
        return this.mClientDAO.findById(codigoCliente);
    }

    @Override
    public TipoRecebimento findTipoRecebimentoByID(final long tipoRecebimento) throws Throwable {
        return this.mTipoRecebimentoDAO.findById(tipoRecebimento);
    }

    @Override
    public Pedido findySaleOrderByID(final Long id) {
        return mPedidoDAO.findById(id);
    }
}
