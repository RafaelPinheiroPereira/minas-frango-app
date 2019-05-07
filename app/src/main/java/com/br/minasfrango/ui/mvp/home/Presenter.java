package com.br.minasfrango.ui.mvp.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.model.Rota;
import com.br.minasfrango.network.tarefa.ExportacaoDeDados;
import com.br.minasfrango.network.tarefa.ImportacaoDeDados;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.activity.RecebimentoActivity;
import com.br.minasfrango.ui.activity.VendasActivity;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IView;
import com.br.minasfrango.util.SessionManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Presenter implements IHomeMVP.IPresenter {

    SessionManager mSessionManager;

    private List<Cliente> clients = new ArrayList<>();

    private List<Rota> routes = new ArrayList<>();

    private IModel model;
    private IView view;

    public Presenter(final IView view) {
        this.view = view;
        this.model = new Model(this);
    }

    @Override
    public boolean checkLogin() {
        this.mSessionManager = new SessionManager(getContext());
        return mSessionManager.checkLogin();
    }

    @Override
    public void closeDrawer() {
        this.view.closerDrawer();
    }

    @Override
    public void dataExport() {
        List<Pedido> pedidos = this.model.obterTodosPedidos();
        new ExportacaoDeDados(this, pedidos).execute();
    }

    @Override
    public void dataImport() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(getUserId());
        funcionario.setNome(getUserName());
        new ImportacaoDeDados(funcionario, this).execute();
    }

    @Override
    public void loadClientsAfterDataImport() {
        obterTodosClientes();
        this.view.loadClientsAfterDataImport();
    }

    @Override
    public void loadRoutesAfterDataImport() {
        obterTodasRotas();
        this.view.loadRoutesAfterDataImport();
    }

    @Override
    public void navigateToReceiptsActivity(Cliente cliente) {
        Intent intent = new Intent(getContext(), RecebimentoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("keyCliente", cliente);
        bundle.putString(
                "dataVenda",
                new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        intent.putExtras(bundle);
        AbstractActivity.navigateToActivity(getContext(), intent);
    }

    @Override
    public List<Rota> obterTodasRotas() {
        routes.clear();
        routes.addAll(model.obterTodasRotas());
        return routes;
    }

    @Override
    public List<Cliente> obterTodosClientes() {
        clients.clear();
        clients.addAll(model.obterTodosClientes());
        return clients;
    }

    public List<Cliente> pesquisarClientePorRota(final Rota route) {
        clients.clear();
        clients.addAll(model.pesquisarClientePorRota(route));
        return clients;
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public String getUserName() {
        return this.mSessionManager.getUserName();
    }

    @Override
    public int getUserId() {
        return this.mSessionManager.getUserID();
    }

    @Override
    public void logout() {
        this.mSessionManager.logout();
    }

    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente(final Cliente cliente) {
        return model.pesquisarRecebimentoPorCliente(cliente);
    }

    @Override
    public void navigateToSalesActivity(final Cliente client) {

        Intent intent = new Intent(getContext(), VendasActivity.class);
        Bundle params = new Bundle();
        params.putSerializable("keyCliente", client);
        params.putLong("keyPedido", 0);
        intent.putExtras(params);
        AbstractActivity.navigateToActivity(getContext(), intent);
    }

    @Override
    public void setAdapters() {
        this.view.setAdapters();
    }

    @Override
    public void hideProgressDialog() {
        this.view.onHideProgressDialog();
    }

    @Override
    public void showDialogLogout() {
        this.view.showDialogLogout();
    }

    @Override
    public void showProgressDialog() {
        this.view.onShowProgressDialog();
    }

    @Override
    public void showDialogClient(final Cliente cliente) {
        this.view.showDialogClient(cliente);
    }

    @Override
    public void showToast(String msg) {
        AbstractActivity.showToast(getContext(), msg);
    }

    @Override
    public void setDrawer(final Bundle savedInstanceState) {
        this.view.setDrawer(savedInstanceState);
    }
}
