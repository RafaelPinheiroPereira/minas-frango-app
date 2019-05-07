package com.br.minasfrango.ui.mvp.login;

import android.content.Context;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.network.RetrofitConfig;
import com.br.minasfrango.network.servico.LoginService;
import com.br.minasfrango.network.tarefa.LoginTask;
import com.br.minasfrango.util.SessionManager;
import retrofit2.Call;

public class Presenter implements ILoginMVP.IPresenter {

    SessionManager mSessionManager;

    private LoginTask mLoginTask;

    private ILoginMVP.IView view;

    public Presenter(final ILoginMVP.IView view) {
        this.view = view;
    }

    @Override
    public Call<Funcionario> autenticateLogin(final String idUser, final String password) {
        return getLoginService().autenticaLogin(Long.parseLong(idUser), password);
    }

    @Override
    public void createSession(
            final String idUser, final String password, final String nameEmployee) {
        this.mSessionManager = new SessionManager(getContext());
        mSessionManager.createUserLoginSession(idUser, password, nameEmployee);
    }

    @Override
    public void doLogin(final String idUser, final String password) {
        mLoginTask = new LoginTask(this, this.view, idUser, password);
        mLoginTask.execute();
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public LoginService getLoginService() {
        return new RetrofitConfig().getLoginService();
    }

    @Override
    public boolean validateLogin() {
        return view.validateForm();
    }
}
