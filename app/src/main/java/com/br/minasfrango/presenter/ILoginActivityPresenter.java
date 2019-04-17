package com.br.minasfrango.presenter;

import android.content.Context;
import com.br.minasfrango.data.pojo.Funcionario;
import com.br.minasfrango.service.LoginService;
import retrofit2.Call;

public interface ILoginActivityPresenter {


    Call<Funcionario> autenticateLogin(String idUser, String password);

    void createSession(String idUser, String password, String nameEmployee);

    void doLogin(String user, String password);

    Context getContext();

    LoginService getLoginService();

    boolean validateLogin();


}
