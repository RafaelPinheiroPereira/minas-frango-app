package com.br.minasfrango.ui.mvp.pedido;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.ClientePedido;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.activity.VendasActivity;
import com.br.minasfrango.ui.activity.VisualizarPedidoActivity;
import com.br.minasfrango.ui.mvp.pedido.IPedidoMVP.IModel;
import com.br.minasfrango.ui.mvp.pedido.IPedidoMVP.IView;
import java.util.List;

public class Presenter implements IPedidoMVP.IPresenter {

    private AlertDialog mAlertDialog;

    private IModel mModel;

    private Pedido mPedido;

    private IView mView;

    private String nomeDaFoto;



    public Presenter(final IView view) {
        this.mView = view;
        this.mModel = new Model(this);
    }

    @Override
    public void cancelarPedido(String motivoCancelamento) {

        this.mModel.cancelarPedido(motivoCancelamento);
        this.mView.dismiss();
    }

    @Override
    public void dismiss() {
        this.mView.dismiss();
    }

    @Override
    public List<ClientePedido> obterTodosClientePedido() {
        return this.mModel.getAllClientePedidos();
    }

    @Override
    public Context getContext() {
        return (Context) this.mView;
    }

    @Override
    public AlertDialog getDialog() {
        return this.mAlertDialog;
    }

    @Override
    public void setDialog(final AlertDialog alertDialog) {
        this.mAlertDialog = alertDialog;
    }

    @Override
    public Pedido getPedido() {
        return this.mPedido;
    }

    @Override
    public String getNomeDaFoto() {
        return nomeDaFoto;
    }
    @Override
    public void setNomeDaFoto(final String nomeDaFoto) {
        this.nomeDaFoto = nomeDaFoto;
    }

    @Override
    public void onNavigateToEditSalesOrderActivity(final Pedido pedido) {

        if (!pedido.isCancelado()) {
            Intent intent = new Intent(getContext(), VendasActivity.class);
            Bundle params = new Bundle();
            params.putSerializable("keyCliente", new Cliente());
            params.putLong("keyPedido", pedido.getIdVenda());
            intent.putExtras(params);
            AbstractActivity.navigateToActivity(getContext(), intent);
        } else {
            AbstractActivity.showToast(getContext(), "PedidoORM já foi cancelado!");
        }
    }

    @Override
    public void onShowDialogDeleteOrderSalle(final Pedido pedido) {
        if (!pedido.isCancelado()) {
            this.mView.showDialogCanceling(pedido);
        } else {
            AbstractActivity.showToast(getContext(), "PedidoORM já foi cancelado!");
        }
    }

    @Override
    public void onNavigateToViewSalesOrderActivity(final Pedido pedido) {
       Intent intent = new Intent(getContext(), VisualizarPedidoActivity.class);
     
        Bundle params = new Bundle();
        params.putLong("keyPedido", pedido.getIdVenda());
        intent.putExtras(params);
        AbstractActivity.navigateToActivity(getContext(), intent);
    }

    @Override
    public void onShowBottoSheetDialog() {
        this.mView.onShowBototoSheetDialog();
    }

    @Override
    public void setPedido(final Pedido pedidoORMSelecionado) {
        this.mPedido = pedidoORMSelecionado;
    }
}
