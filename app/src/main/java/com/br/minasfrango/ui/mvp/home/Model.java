package com.br.minasfrango.ui.mvp.home;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.ClienteGrupoDAO;
import com.br.minasfrango.data.dao.EmpresaDAO;
import com.br.minasfrango.data.dao.FuncionarioDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.realm.ClienteGrupoORM;
import com.br.minasfrango.data.realm.ClienteORM;
import com.br.minasfrango.data.realm.EmpresaORM;
import com.br.minasfrango.data.realm.FuncionarioORM;
import com.br.minasfrango.data.realm.PedidoORM;
import com.br.minasfrango.data.realm.RecebimentoORM;
import com.br.minasfrango.ui.mvp.home.IHomeMVP.IModel;
import java.util.List;

public class Model implements IModel {

    ClienteDAO mClienteDAO = ClienteDAO.getInstace(ClienteORM.class);

    PedidoDAO mOrderDAO = PedidoDAO.getInstace(PedidoORM.class);

    RecebimentoDAO mRecebimentoDAO = RecebimentoDAO.getInstace(RecebimentoORM.class);

    ClienteGrupoDAO mClienteGrupoDAO = ClienteGrupoDAO.getInstace(ClienteGrupoORM.class);

    EmpresaDAO mEmpresaDAO =EmpresaDAO.getInstace(EmpresaORM.class);
    FuncionarioDAO mFuncionarioDAO=FuncionarioDAO.getInstace(FuncionarioORM.class);

    private Presenter mPresenter;

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void deletarFuncionarioDaSessao() {
        FuncionarioORM funcionarioORM =this.mFuncionarioDAO.where().findFirst();
        Funcionario funcionarioDeletado=new Funcionario(funcionarioORM);
        this.mFuncionarioDAO.deletar(new FuncionarioORM(funcionarioDeletado));
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

    @Override
    public Empresa pesquisarEmpresaRegistrada() {
        return mEmpresaDAO.pesquisarEmpresaRegistradaNoDispositivo();
    }

    @Override
    public Funcionario pesquisarFuncionarioDaSessao() {

        FuncionarioORM funcionarioORM = mFuncionarioDAO.where().findFirst();
        Funcionario funcionario= new Funcionario(funcionarioORM);
        return funcionario;
    }

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente(final Cliente cliente) {
        return mRecebimentoDAO.pesquisarRecebimentoPorCliente(cliente);
    }
}