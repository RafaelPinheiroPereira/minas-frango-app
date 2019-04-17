package com.br.minasfrango.model;

import com.br.minasfrango.presenter.PedidoActivityPresenter;

public class PedidoActivityModel implements IPedidoActivityModel {

    private PedidoActivityPresenter mPresenter;

    public PedidoActivityModel(final PedidoActivityPresenter presenter) {
        mPresenter = presenter;
    }
}
