package com.br.minasfrango.ui.mvp.home;

import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.dao.RouteDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.model.Rota;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.data.realm.RecebimentoORM;
import com.br.minasfrango.data.realm.RotaORM;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import java.util.List;

public class Model implements IModel {

    ClientDAO mClientDAO = ClientDAO.getInstace(ClienteORM.class);

    PedidoDAO mOrderDAO = PedidoDAO.getInstace(PedidoORM.class);

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace(RecebimentoORM.class);

    RouteDAO mRouteDAO = RouteDAO.getInstace(RotaORM.class);

    private Presenter mPresenter;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public List<Rota> obterTodasRotas() {
        return mRouteDAO.todos();
    }

    @Override
    public List<Cliente> obterTodosClientes() {
        return mClientDAO.todos();
    }

    @Override
    public List<Pedido> obterTodosPedidos() {

        return mOrderDAO.todos();
    }

    public List<Cliente> pesquisarClientePorRota(final Rota rota) {
        return mClientDAO.pesquisarClientePorRota(rota);
    }

    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente(final Cliente cliente) {
        return mRecebimentoDAO.pesquisarRecebimentoPorCliente(cliente);
    }
}
