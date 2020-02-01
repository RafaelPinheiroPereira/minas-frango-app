package com.br.minasfrango.ui.mvp.configuracao;

import android.content.Context;
import com.br.minasfrango.data.model.Configuracao;
import com.br.minasfrango.util.ArquivoUtils;

public interface IConfiguracaoMVP {

    interface IPresenter {

        void criarPastasDasImagens();

        Context getContext();


        String statusSistema();

        void realizarConfiguracao();

        void salvarConfiguracoes(final Configuracao configuracao);

        String getMac();

        String getCnpj();

        void setMac(String mac);

        void setCnpj(String cnpj);

        ArquivoUtils getArquivoUtils();

        void setArquivoUtils(ArquivoUtils arquivoUtils);
    }

    interface IModel {

        String statusSistema();

        void salvarConfiguracao(final Configuracao configuracao);
    }

    interface IView {

        boolean estaVazioOCNPJ();
    }
}
