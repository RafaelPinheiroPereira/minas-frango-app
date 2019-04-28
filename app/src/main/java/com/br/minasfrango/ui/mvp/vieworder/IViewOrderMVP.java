package com.br.minasfrango.ui.mvp.vieworder;

import android.os.Bundle;
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.Pedido;
import com.br.minasfrango.data.realm.TipoRecebimento;

public interface IViewOrderMVP {

    interface IPresenter {

        Cliente findClientByID(long codigoCliente);

        TipoRecebimento findTipoRecebimentoByID(long tipoRecebimento) throws Throwable;

        Cliente getCliente();

        void setCliente(Cliente cliente);

        Pedido getPedido();

        void setPedido(Pedido pedido);

        Pedido getSaleOrderParams(final Bundle extras);

        TipoRecebimento getTipoRecebimento();

        void setTipoRecebimento(TipoRecebimento tipoRecebimento);

        void setDataView();
    }

    interface IModel {

        Cliente findClientByID(long codigoCliente);

        TipoRecebimento findTipoRecebimentoByID(long tipoRecebimento) throws Throwable;

        Pedido findySaleOrderByID(Long id);
    }

    interface IView {

        void setDataView();
    }

}