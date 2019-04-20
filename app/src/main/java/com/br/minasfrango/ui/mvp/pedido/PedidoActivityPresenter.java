package com.br.minasfrango.ui.mvp.pedido;

import android.content.Context;
import com.br.minasfrango.util.SessionManager;

public class PedidoActivityPresenter implements IPedidoActivityPresenter {

    private IPedidoActivityModel mModel;

    private IPedidoActivityView mView;

    public PedidoActivityPresenter(final IPedidoActivityView view) {
        this.mView = view;
        this.mModel = new PedidoActivityModel(this);
    }

    @Override
    public Context getContext() {
        return (Context)this.mView;
    }




    @Override
    public String getUserName() {
        return  new SessionManager(getContext()).getUserName();
    }


    @Override
    public void showToast(final String s) {

    }


}
