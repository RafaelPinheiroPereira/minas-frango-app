package com.br.minasfrango.ui.mvp.exclusao;

import java.util.Date;

public interface IExclusaoMVP {
    interface IPresenter {

        void exibirMensagemErro();

        void setDataInicial(Date dataInicial);

        Date getDataInicial();

        void setDataFinal(Date dataFinal);

        Date getDataFinal();

        void excluirPedido();

        void exibirMensagemSucesso();
    }

    interface IModel {

        void excluirPedido();
    }

    interface IView {
        void exibirMensagemSucesso();
        void  exibirMensagemErro();
    }
}
