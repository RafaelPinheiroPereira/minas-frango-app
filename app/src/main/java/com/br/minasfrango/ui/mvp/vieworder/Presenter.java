package com.br.minasfrango.ui.mvp.vieworder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.Pedido;
import com.br.minasfrango.data.realm.TipoRecebimento;
import com.br.minasfrango.ui.mvp.vieworder.IViewOrderMVP.IView;
import com.br.minasfrango.util.ImpressoraUtil;

public class Presenter implements IViewOrderMVP.IPresenter {

    Cliente mCliente;

    IViewOrderMVP.IModel mModel;

    Pedido mPedido;

    TipoRecebimento mTipoRecebimento;

    IViewOrderMVP.IView mView;

    ImpressoraUtil mImpressoraUtil;

    public Presenter(final IView view) {
        mView = view;
        mModel = new Model(this);
        mImpressoraUtil = new ImpressoraUtil((Activity) getContext());
    }

    @Override
    public Cliente findClientByID(final long codigoCliente) {
        return this.mModel.findClientByID(codigoCliente);
    }

    @Override
    public TipoRecebimento findTipoRecebimentoByID(final long tipoRecebimento) throws Throwable {
        return this.mModel.findTipoRecebimentoByID(tipoRecebimento);
    }

    @Override
    public Cliente getCliente() {
        return mCliente;
    }

    @Override
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

    @Override
    public Pedido getSaleOrderParams(final Bundle extras) {
        long id = extras.getLong("keyPedido");
        return this.mModel.findySaleOrderByID(id);
    }

    @Override
    public TipoRecebimento getTipoRecebimento() {
        return mTipoRecebimento;
    }

    @Override
    public void setTipoRecebimento(final TipoRecebimento tipoRecebimento) {
        mTipoRecebimento = tipoRecebimento;
    }

    @Override
    public void setDataView() {
        this.mView.setDataView();
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
}
