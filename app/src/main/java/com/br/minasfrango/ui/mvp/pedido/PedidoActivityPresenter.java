package com.br.minasfrango.ui.mvp.pedido;

import android.content.Context;
import android.os.Bundle;
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
    public void hideProgressDialog() {
            this.mView.hideProgressDialog();
    }

    @Override
    public void logout() {
        new SessionManager(getContext()).logout();
    }

    @Override
    public void setDrawer(final Bundle savedInstanceState) {
        this.mView.setDrawer(savedInstanceState);
    }

    @Override
    public String getUserName() {
        return  new SessionManager(getContext()).getUserName();
    }

    @Override
    public void onShowProgressDialog() {

    }

    @Override
    public void showToast(final String s) {

    }


}
