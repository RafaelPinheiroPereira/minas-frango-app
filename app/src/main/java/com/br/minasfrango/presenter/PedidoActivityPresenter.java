package com.br.minasfrango.presenter;

import android.os.Bundle;
import com.br.minasfrango.model.IPedidoActivityModel;
import com.br.minasfrango.model.PedidoActivityModel;
import com.br.minasfrango.view.IPedidoActivityView;

public class PedidoActivityPresenter implements IPedidoActivityPresenter {

    private IPedidoActivityModel mModel;

    private IPedidoActivityView mView;

    public PedidoActivityPresenter(final IPedidoActivityView view) {
        this.mView = view;
        this.mModel = new PedidoActivityModel(this);
    }

    @Override
    public void setDrawer(final Bundle savedInstanceState) {
        this.mView.setDrawer(savedInstanceState);
    }
}
