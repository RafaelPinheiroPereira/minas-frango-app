package com.br.minasfrango.ui.mvp.login;

import android.content.Context;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Nucleo;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.servico.AutenticacaoService;
import com.br.minasfrango.network.tarefa.LoginTask;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IModel;
import com.br.minasfrango.util.ControleSessao;
import java.util.List;
import retrofit2.Call;

public class Presenter implements ILoginMVP.IPresenter {

    ControleSessao mControleSessao;

    private LoginTask mLoginTask;

    private ILoginMVP.IView view;

    private IModel mIModel;

    private Nucleo mNucleo;

    private Empresa mEmpresa;

    public Presenter(final ILoginMVP.IView view) {
        this.view = view;
        this.mIModel = new Model(this);
    }

    @Override
    public Call<Funcionario> autenticarLogin(final String id, final String senha, long idEmpresa) {
        return getServicoLogin().autenticar(Long.parseLong(id), senha, idEmpresa);
    }

    @Override
    public List<Nucleo> carregarTodosOsNucleos() {
        return this.mIModel.pesquisarTodosNucleos();
    }

    @Override
    public void criarSessao(final String id, final String senha, final String nome, long idNucleo, long maxIdVenda) {
        this.mControleSessao = new ControleSessao(getContexto());
        mControleSessao.criarSessao(id, senha, nome, idNucleo,maxIdVenda);
    }

    @Override
    public Empresa pesquisarEmpresaRegistrada() {
        return this.mIModel.pesquisarEmpresaRegistrada();
    }

    @Override
    public void realizarLogin(final String idUser, final String password) {
        mLoginTask = new LoginTask(this, this.view, idUser, password);
        mLoginTask.execute();
    }

    @Override
    public Context getContexto() {
        return (Context) view;
    }

    @Override
    public AutenticacaoService getServicoLogin() {
        return new RetrofitConfig().getLoginService();
    }

    @Override
    public boolean loginValidado() {
        return view.validarForm();
    }

    @Override
    public void salvarFuncionario(final Funcionario funcionario) {
        this.mIModel.salvarFuncionario(funcionario);
    }

    @Override
    public Empresa getEmpresa() {
        return mEmpresa;
    }

    @Override
    public void setEmpresa(final Empresa empresa) {
        mEmpresa = empresa;
    }

    @Override
    public Nucleo getNucleo() {
        return mNucleo;
    }

    @Override
    public void setNucleo(final Nucleo nucleo) {
        mNucleo = nucleo;
    }
}
