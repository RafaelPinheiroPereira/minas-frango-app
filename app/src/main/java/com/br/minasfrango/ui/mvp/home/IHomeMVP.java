package com.br.minasfrango.ui.mvp.home;

import android.content.Context;
import android.os.Bundle;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.model.Rota;
import java.util.List;

public interface IHomeMVP {

    interface IPresenter {

        void esconderProgressDialog();

        void exibirDialogClient(final Cliente cliente);

        void exibirDialogLogout();

        List<Rota> obterTodasRotas();

        List<Cliente> obterTodosClientes();

        List<Cliente> pesquisarClientePorRota(Rota rota);

        List<Recebimento> pesquisarRecebimentoPorCliente(Cliente cliente);

        void exibirProgressDialog();

        void exibirToast(String msg);

        Context getContext();

        void exportar();

        void fecharDrawer();

        int getUserId();

        void logout();

        void setDrawer(final Bundle savedInstanceState);

        String getNomeUsuario();

        void importar();

        void setAdapters();

        void navigateToReceiptsActivity(Cliente cliente);

        void navigateToSalesActivity(Cliente cliente);

        void obterClientesAposImportarDados();

        void obterRotasAposImportarDados();

        boolean verificarLogin();
    }

    interface IView {

        void fecharDrawer();

        void setAdapters();

        void setDrawer(final Bundle savedInstanceState);

        void showDialogClient(final Cliente cliente);

        void onShowProgressDialog();

        void onHideProgressDialog();

        void obterClientesAposImportarDados();

        void obterRotasAposImportarDados();

        void showDialogLogout();
    }

    interface IModel {

        List<Rota> obterTodasRotas();

        List<Cliente> obterTodosClientes();

        List<Pedido> obterTodosPedidos();

        List<Cliente> pesquisarClientePorRota(Rota rota);

        List<Recebimento> pesquisarRecebimentoPorCliente(Cliente cliente);
    }
}
