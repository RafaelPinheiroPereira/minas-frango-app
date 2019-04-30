package com.br.minasfrango.ui.mvp.home;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.dao.RouteDAO;
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.Pedido;
import com.br.minasfrango.data.realm.Recebimento;
import com.br.minasfrango.data.realm.Rota;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import java.util.List;

public class Model implements IModel {

    ClientDAO mClientDAO = ClientDAO.getInstace(Cliente.class);

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace(Recebimento.class);

    PedidoDAO mOrderDAO = PedidoDAO.getInstace(Pedido.class);

    RouteDAO mRouteDAO = RouteDAO.getInstace(Rota.class);

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
        return mRecebimentoDAO.pesquisarRecebimentoPorCliente(cliente);
    }

    @Override
    public List<Cliente> getAllClients() {
        return mClientDAO.getAll();
    }

    @Override
    public List<Pedido> getAllOrders() {

        return mOrderDAO.findAll();
    }

    @Override
    public List<Rota> getAllRoutes() {
        return mRouteDAO.allRoutes();
    }
}
