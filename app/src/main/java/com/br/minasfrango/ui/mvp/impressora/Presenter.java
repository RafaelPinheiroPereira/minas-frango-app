package com.br.minasfrango.ui.mvp.impressora;

import android.content.Context;
import com.br.minasfrango.util.ControleSessao;

public class Presenter implements IImpressoraMVP.IPresenter {

    private IImpressoraMVP.IModel model;

    private IImpressoraMVP.IView view;

    private ControleSessao mControleSessao;

    public Presenter(final IImpressoraMVP.IView view) {
        this.view = view;
        this.model = new Model(this);
    }

    @Override
    public Context getContext() {
        return (Context) this.view;
    }
}
