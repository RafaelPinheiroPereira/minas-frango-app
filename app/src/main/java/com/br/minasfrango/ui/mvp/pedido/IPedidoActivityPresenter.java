package com.br.minasfrango.ui.mvp.pedido;

import android.content.Context;
import android.os.Bundle;


public interface IPedidoActivityPresenter  {

    void hideProgressDialog();

    void logout();

    void setDrawer(final Bundle savedInstanceState);
    String getUserName();
    Context getContext();

    void onShowProgressDialog();

    void showToast(String s);
}
