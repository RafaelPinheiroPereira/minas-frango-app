package com.br.minasfrango.ui.mvp.login;

import android.content.Context;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.servico.ServicoLogin;
import com.br.minasfrango.network.tarefa.LoginTask;
import com.br.minasfrango.util.ControleSessao;
import retrofit2.Call;

public class Presenter implements ILoginMVP.IPresenter {

    ControleSessao mControleSessao;

    private LoginTask mLoginTask;

    private ILoginMVP.IView view;

    public Presenter(final ILoginMVP.IView view) {
        this.view = view;
    }

    @Override
    public Call<Funcionario> autenticarLogin(final String id, final String senha) {
        return getServicoLogin().autenticarLogin(Long.parseLong(id), senha);
    }

    @Override
    public void criarSessao(
            final String id, final String senha, final String nome) {
        this.mControleSessao = new ControleSessao(getContexto());
        mControleSessao.criarSessao(id, senha, nome);
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
    public ServicoLogin getServicoLogin() {
        return new RetrofitConfig().getLoginService();
    }

    @Override
    public boolean loginValidado() {
        return view.validarForm();
    }
}
