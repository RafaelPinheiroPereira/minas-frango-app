package com.br.minasfrango.ui.mvp.exclusao;

import com.br.minasfrango.ui.mvp.exclusao.IExclusaoMVP.IView;
import java.util.Date;

public class Presenter implements IExclusaoMVP.IPresenter {

    private final Model mModel;

    private final IView mView;

    Date dataInicial;
    Date dataFinal;

    public Presenter(final IView view) {
        mView = view;
        mModel = new Model(this);
    }

    @Override
    public void exibirMensagemErro() {
        this.mView.exibirMensagemErro();

    }

    @Override
    public void setDataInicial(final Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    @Override
    public Date getDataInicial() {
        return this.dataInicial;
    }

    @Override
    public void setDataFinal(final Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    @Override
    public Date getDataFinal() {
        return this.dataFinal;
    }

    @Override
    public void excluirPedido() {
        this.mModel.excluirPedido();
    }

     @Override
     public void exibirMensagemSucesso() {
         this.mView.exibirMensagemSucesso();
     }
 }
