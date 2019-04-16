package com.br.minasfrango.presenter;

import android.content.Context;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.service.LoginService;
import com.br.minasfrango.tasks.LoginTask;
import com.br.minasfrango.util.RetrofitConfig;
import com.br.minasfrango.util.SessionManager;
import com.br.minasfrango.view.ILoginView;
import retrofit2.Call;

public class LoginPresenter implements ILoginPresenter {

    SessionManager mSessionManager;

    private LoginTask mLoginTask;

    private ILoginView view;

    public LoginPresenter(final ILoginView view) {
        this.view = view;
    }

    @Override
    public Call<Funcionario> autenticateLogin(final String idUser, final String password) {
        return getLoginService().autenticaLogin(Long.parseLong(idUser), password);
    }

    @Override
    public void createSession(final String idUser, final String password, final String nameEmployee) {
        this.mSessionManager = new SessionManager(getContext());
        mSessionManager.createUserLoginSession(idUser,
                password,
                nameEmployee);
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
