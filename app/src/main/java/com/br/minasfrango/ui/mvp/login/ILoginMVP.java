package com.br.minasfrango.ui.mvp.login;

import android.content.Context;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.network.servico.LoginService;
import retrofit2.Call;

public interface ILoginMVP {

    interface IPresenter {

        Call<Funcionario> autenticarLogin(String idUser, String password);

        void criarSessaoUsuario(String idUser, String password, String nameEmployee);

        void iniciarAnimacaoTela();

        void realizarLogin(String user, String password);

        Context getContext();

        LoginService getLoginService();

        boolean validarLogin();

        void verificarPermissoes();
    }

    interface IView {

        void iniciarAnimacao();

        void iniciarAnimacaoTela();

        boolean validarEntradaDados();

        void verificarPermissoes();
    }
}
