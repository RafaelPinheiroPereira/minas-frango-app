package com.br.minasfrango.ui.mvp.pedido;

public class PedidoActivityModel implements IPedidoActivityModel {

    private PedidoActivityPresenter mPresenter;

    public PedidoActivityModel(final PedidoActivityPresenter presenter) {
        mPresenter = presenter;
    }
}
