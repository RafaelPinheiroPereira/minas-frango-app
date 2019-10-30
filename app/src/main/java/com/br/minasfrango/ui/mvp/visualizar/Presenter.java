package com.br.minasfrango.ui.mvp.visualizar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.ui.mvp.visualizar.IViewOrderMVP.IView;
import com.br.minasfrango.util.ImpressoraUtil;

public class Presenter implements IViewOrderMVP.IPresenter {

    Cliente mCliente;

    IViewOrderMVP.IModel mModel;

    Pedido mPedido;



    IViewOrderMVP.IView mView;

    ImpressoraUtil mImpressoraUtil;

    public Presenter(final IView view) {
        mView = view;
        mModel = new Model(this);
        mImpressoraUtil = new ImpressoraUtil((Activity) getContext());
    }

    @Override
    public Pedido getParametrosDaVenda(final Bundle extras) {
        long id = extras.getLong("keyPedido");

        return this.mModel.pesquisarVendaPorId(id);
    }

    @Override
    public Cliente pesquisarClientePorID(final long codigoCliente) {
        return this.mModel.pesquisarClientePorID(codigoCliente);
    }

    @Override
    public Cliente getCliente() {
        return mCliente;
    }

    public void setCliente(final Cliente cliente) {
        mCliente = cliente;
    }

    @Override
    public Pedido getPedido() {
        return mPedido;
    }

    @Override
    public void setPedido(final Pedido pedido) {
        mPedido = pedido;
    }

    /**
     * Metodos relacionados a impressao
     */
    @Override
    public void esperarPorConexao() {
        if (this.mImpressoraUtil.esperarPorConexao()) {
            this.mView.exibirBotaoGerarRecibo();
        }
    }



    @Override
    public void setDataView() {
        this.mView.setDataView();
    }



    @Override
    public void fecharConexaoAtiva() {
        this.mImpressoraUtil.fecharConexaoAtiva();
    }

    @Override
    public Context getContext() {
        return (Context) this.mView;
    }

    @Override
    public void imprimirComprovante() {
        this.mImpressoraUtil.imprimirComprovantePedido(getPedido(), getCliente());
    }

    @Override
    public void exibirBotaoFotografar() {
        this.mView.exibirBotaoFotografar();
    }
}
