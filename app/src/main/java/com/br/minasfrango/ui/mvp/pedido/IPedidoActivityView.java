package com.br.minasfrango.ui.mvp.pedido;

import android.os.Bundle;

public interface IPedidoActivityView  {

    void hideProgressDialog();

    void setDrawer(final Bundle savedInstanceState);
    void confirmAlertDialog();
}
