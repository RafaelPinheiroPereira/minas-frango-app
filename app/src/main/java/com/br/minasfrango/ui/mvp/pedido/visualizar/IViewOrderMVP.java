package com.br.minasfrango.ui.mvp.pedido.visualizar;

import android.content.Context;
import android.os.Bundle;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.TipoRecebimento;

public interface IViewOrderMVP {

    interface IPresenter {

        Pedido getVendaParams(final Bundle extras);

        Cliente pesquisarClientePorID(long codigoCliente);

        Cliente getCliente();

        void setCliente(Cliente cliente);

        Pedido getPedido();

        void setPedido(Pedido pedido);

        TipoRecebimento pesquisarTipoRecebimentoPorId(long tipoRecebimento) throws Throwable;

        TipoRecebimento getTipoRecebimento();

        void setTipoRecebimento(TipoRecebimento tipoRecebimento);

        void setDataView();

        /**
         * Metodos relacionados a impressao
         */
        void esperarPorConexao();

        void fecharConexaoAtiva();

        Context getContext();

        void imprimirComprovante();
    }

    interface IModel {

        Cliente pesquisarClientePorID(long codigoCliente);

        TipoRecebimento pesquisarTipoRecebimentoPorID(long tipoRecebimento) throws Throwable;

        Pedido pesquisarVendaPorId(Long id);
    }

    interface IView {

        void exibirBotaoGerarRecibo();

        void setDataView();
    }

}
