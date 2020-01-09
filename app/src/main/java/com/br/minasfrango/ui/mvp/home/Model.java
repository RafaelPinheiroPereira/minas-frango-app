package com.br.minasfrango.ui.mvp.home;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.ClienteGrupoDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.realm.ClienteGrupoORM;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.data.realm.RecebimentoORM;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import java.util.List;

public class Model implements IModel {

    ClienteDAO mClienteDAO = ClienteDAO.getInstace(ClienteORM.class);

    PedidoDAO mOrderDAO = PedidoDAO.getInstace(PedidoORM.class);

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace(RecebimentoORM.class);

    ClienteGrupoDAO mClienteGrupoDAO = ClienteGrupoDAO.getInstace(ClienteGrupoORM.class);

    private Presenter mPresenter;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<ClienteGrupo> obterTodasRedes() {
        return mClienteGrupoDAO.todos();
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

    @Override
    public List<Recebimento> obterTodosRecebimentos() {
        return this.mRecebimentoDAO.pesquisarTodosRecebimentos();
    }

    public List<Cliente> pesquisarClientePorRede(final ClienteGrupo clienteGrupo) {
        return mClienteDAO.pesquisarClientePorRede(clienteGrupo);
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente(final Cliente cliente) {
        return mRecebimentoDAO.pesquisarRecebimentoPorCliente(cliente);
    }
}