package com.br.minasfrango.ui.mvp.home;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import com.br.minasfrango.data.dao.ClienteDAO;
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

    ClienteDAO mClienteDAO = ClienteDAO.getInstace(ClienteORM.class);

    PedidoDAO mOrderDAO = PedidoDAO.getInstace(PedidoORM.class);

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace(RecebimentoORM.class);

    RouteDAO mRouteDAO = RouteDAO.getInstace(RotaORM.class);

    private Presenter mPresenter;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<Rota> obterTodasRotas() {
        return mRouteDAO.todos();
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<Cliente> obterTodosClientes() {
        return mClienteDAO.todos();
    }

    @Override
    public List<Pedido> obterTodosPedidos() {

        return mOrderDAO.todos();
    }

    public List<Cliente> pesquisarClientePorRota(final Rota rota) {
        return mClienteDAO.pesquisarClientePorRota(rota);
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente(final Cliente cliente) {
        return mRecebimentoDAO.pesquisarRecebimentoPorCliente(cliente);
    }
}