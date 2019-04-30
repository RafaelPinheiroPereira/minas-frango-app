package com.br.minasfrango.ui.mvp.impressora;

import android.content.Context;

public class Presenter implements IImpressoraMVP.IPresenter {

    private IImpressoraMVP.IModel model;

    private IImpressoraMVP.IView view;

    public Presenter(final IImpressoraMVP.IView view) {
        this.view = view;
        this.model = new Model(this);
    }

    @Override
    public Context getContext() {
        return (Context) this.view;
    }
}
