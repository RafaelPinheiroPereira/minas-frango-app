package com.br.minasfrango.ui.mvp.pedido;

import android.app.AlertDialog;
import android.content.Context;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.ClientePedido;
import com.br.minasfrango.data.pojo.Pedido;
import java.util.List;


public interface IPedidoMVP {


    interface IPresenter {

        void cancelPedido(String cancelingMotive);

        void dismiss();

        List<ClientePedido> getAllCientePedidos();

        Context getContext();

        AlertDialog getDialog();

        void setDialog(AlertDialog alertDialog);

        Pedido getPedido();

        void setPedido(Pedido pedidoSelecionado);

        void onNavigateToEditSalesOrderActivity(Pedido pedido);

        void onNavigateToViewSalesOrderActivity(Pedido pedido);

        void onShowBottoSheetDialog();

        void onShowDialogDeleteOrderSalle(Pedido pedido);
    }

    interface IView {

        void dismiss();

        void onShowBototoSheetDialog();

        void showDialogCanceling(Pedido pedido);
    }

    interface IModel {

        List<Pedido> allPedidos();

        void cancelOrder(String cancelingMotive);

        List<Cliente> findClientByPedidos(List<Pedido> pedidos);

        List<ClientePedido> getAllClientePedidos();


    }
}
