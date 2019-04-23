package com.br.minasfrango.ui.mvp.login;

import android.content.Context;
import com.br.minasfrango.data.realm.Funcionario;
import com.br.minasfrango.network.service.LoginService;
import retrofit2.Call;

public interface ILoginMVP {

    interface IPresenter {

        Call<Funcionario> autenticateLogin(String idUser, String password);

        void createSession(String idUser, String password, String nameEmployee);

        void doLogin(String user, String password);

        Context getContext();
        LoginService getLoginService();

        boolean validateLogin();


    }

    interface IView {

        void loadAnimation();
        boolean validateForm();

    }
}
