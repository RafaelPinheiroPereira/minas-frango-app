package com.br.minasfrango.ui.mvp.vieworder;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.PedidoORM;

public class Model implements IViewOrderMVP.IModel {

    ClientDAO mClientDAO = ClientDAO.getInstace(ClienteORM.class);

    PedidoDAO mPedidoDAO = PedidoDAO.getInstace(PedidoORM.class);

    IViewOrderMVP.IPresenter mPresenter;

    TipoRecebimentoDAO mTipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();

    public Model(final Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public Cliente findClientByID(final long codigoCliente) {
        return new Cliente(this.mClientDAO.findById(codigoCliente));
    }

    @Override
    public TipoRecebimento findTipoRecebimentoByID(final long tipoRecebimento) throws Throwable {
        return this.mTipoRecebimentoDAO.findById(tipoRecebimento);
    }

    @Override
    public Pedido findySaleOrderByID(final Long id) {
        PedidoORM pedidoORM = mPedidoDAO.findById(id);
        Pedido pedido = new Pedido(pedidoORM);
        pedido.setItens(Pedido.converterListItemPedidoRealmParaModel(pedidoORM));
        return pedido;
    }
}
