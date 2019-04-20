package com.br.minasfrango.ui.mvp.pedido;

import android.content.Context;


public interface IPedidoActivityPresenter  {



    String getUserName();
    Context getContext();
    void showToast(String s);
}
