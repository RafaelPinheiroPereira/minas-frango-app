package com.br.minasfrango.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.br.minasfrango.activity.RecebimentoActivity;
import com.br.minasfrango.activity.VendasActivity;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Recebimento;
import com.br.minasfrango.data.pojo.Rota;
import com.br.minasfrango.model.HomeActivityModel;
import com.br.minasfrango.model.IHomeActivityModel;
import com.br.minasfrango.util.SessionManager;
import com.br.minasfrango.view.IHomeActivityView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivityPresenter implements IHomeActivityPresenter {


    SessionManager mSessionManager;

    private List<Cliente> clients = new ArrayList<>();

    private IHomeActivityModel model;

    private IHomeActivityView view;

    public HomeActivityPresenter(final IHomeActivityView view) {
        this.view = view;
        this.model = new HomeActivityModel(this);
    }


    @Override
    public boolean checkLogin() {
        this.mSessionManager = new SessionManager(getContext());
        return mSessionManager.checkLogin();
    }

    @Override
    public List<Cliente> findClientsByRoute(final Rota route) {
        clients.clear();
        clients.addAll(model.findClientsByRoute(route));
        return clients;
    }

    @Override
    public List<Recebimento> findReceiptsByClient(final Cliente cliente) {
        return model.findReceiptsByClient(cliente);
    }

    @Override
    public List<Cliente> getAllClients() {
        clients.clear();
        clients.addAll(model.getAllClients());
        return clients;
    }

    @Override
    public List<Rota> getAllRoutes() {
        return model.getAllRoutes();
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
    public void logout() {
        this.mSessionManager.logout();
    }

    @Override
    public void navigateToReceiptsActivity(Cliente cliente) {
        Intent intent = new Intent(getContext(), RecebimentoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("keyCliente", cliente);
        bundle.putString("dataVenda",
                new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
        intent.putExtras(bundle);
        this.view.navigateToActivity(intent);
    }

    @Override
    public void navigateToSalesActivity(final Cliente client) {

        Intent intent = new Intent(getContext(), VendasActivity.class);
        Bundle params = new Bundle();
        params.putSerializable("keyCliente", client);
        params.putLong("keyPedido", 0);
        intent.putExtras(params);
        this.view.navigateToActivity(intent);

    }

    @Override
    public void setAdapters() {
        this.view.setAdapters();
    }

    @Override
    public void setDrawer(final Bundle savedInstanceState) {
        this.view.setDrawer(savedInstanceState);
    }


}
