package com.br.minasfrango.ui.mvp.login;

import android.content.Context;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.network.servico.ServicoLogin;
import retrofit2.Call;

public interface ILoginMVP {

    interface IPresenter {

        Call<Funcionario> autenticarLogin(String idUser, String password);

        void criarSessao(String idUser, String password, String nameEmployee);

        void realizarLogin(String user, String password);

        Context getContexto();

        ServicoLogin getServicoLogin();

        boolean loginValidado();
    }

    interface IView {

        void carregarAnimacaoInicializacao();

        boolean validarForm();
    }
}