package com.br.minasfrango.ui.mvp.configuracao;

import android.content.Context;
import com.br.minasfrango.data.model.Configuracao;

public interface IConfiguracaoMVP {

    interface IPresenter {

        Context getContext();



        String statusSistema();

        public void realizarConfiguracao();

        void salvarConfiguracoes(final Configuracao configuracao);

        String getMac();

        String getCnpj();

        void setMac(String mac);

        void setCnpj(String cnpj);
    }

    interface IModel {



        String statusSistema();

        void salvarConfiguracao(final Configuracao configuracao);
    }

    interface IView {

        public boolean estaVazioOCNPJ();
    }
}
