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

        void closeDrawer();

        void dataExport();

        void dataImport();

        List<Rota> obterTodasRotas();

        List<Cliente> obterTodosClientes();

        List<Cliente> pesquisarClientePorRota(Rota rota);

        List<Recebimento> pesquisarRecebimentoPorCliente(Cliente cliente);

        void loadClientsAfterDataImport();

        void loadRoutesAfterDataImport();

        Context getContext();

        String getUserName();

        boolean checkLogin();

        int getUserId();

        void logout();

        void setDrawer(final Bundle savedInstanceState);

        void navigateToReceiptsActivity(Cliente client);

        void navigateToSalesActivity(Cliente client);

        void setAdapters();

        void hideProgressDialog();

        void showDialogLogout();

        void showProgressDialog();

        void showDialogClient(final Cliente cliente);

        void showToast(String msg);
    }

    interface IView {

        void closerDrawer();

        void setAdapters();

        void setDrawer(final Bundle savedInstanceState);

        void showDialogClient(final Cliente cliente);

        void onShowProgressDialog();

        void onHideProgressDialog();

        void loadClientsAfterDataImport();

        void loadRoutesAfterDataImport();

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
