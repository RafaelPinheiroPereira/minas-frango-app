package com.br.minasfrango.ui.mvp.home;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.dao.RouteDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Recebimento;
import com.br.minasfrango.data.pojo.Rota;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import java.util.List;

public class Model implements IModel {

    ClientDAO mClientDAO = ClientDAO.getInstace(Cliente.class);

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace(Recebimento.class);

    RouteDAO mRotaDAO = RouteDAO.getInstace(Rota.class);

    private Presenter mPresenter;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public List<Cliente> findClientsByRoute(final Rota rota) {
        return mClientDAO.findClientsRoute(rota);
    }

    @Override
    public List<Recebimento> findReceiptsByClient(final Cliente cliente) {
        return mRecebimentoDAO.findReceiptsByClient(cliente);
    }

    @Override
    public List<Cliente> getAllClients() {
        return mClientDAO.loadAll();
    }

    @Override
    public List<Rota> getAllRoutes() {
        return mRotaDAO.allRoutes();
    }
}
