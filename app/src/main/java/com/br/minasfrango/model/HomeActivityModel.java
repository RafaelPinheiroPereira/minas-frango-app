package com.br.minasfrango.model;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.dao.RotaDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Recebimento;
import com.br.minasfrango.data.pojo.Rota;
import com.br.minasfrango.presenter.HomeActivityPresenter;
import java.util.List;

public class HomeActivityModel implements IHomeActivityModel {

    ClientDAO mClientDAO = ClientDAO.getInstace();

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace();

    RotaDAO mRotaDAO = RotaDAO.getInstace();

    private HomeActivityPresenter mPresenter;

    public HomeActivityModel(final HomeActivityPresenter presenter) {
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
        return mClientDAO.allClients();
    }

    @Override
    public List<Rota> getAllRoutes() {
        return mRotaDAO.allRoutes();
    }
}
