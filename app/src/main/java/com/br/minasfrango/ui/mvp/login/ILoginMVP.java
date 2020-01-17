package com.br.minasfrango.ui.mvp.login;

import android.content.Context;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Nucleo;
import com.br.minasfrango.network.servico.AutenticacaoService;
import java.util.List;
import retrofit2.Call;

public interface ILoginMVP {

    interface IPresenter {



        Call<Funcionario> autenticarLogin(String idUser, String password,long idEmpresa);

        List<Nucleo> carregarTodosOsNucleos();

        void criarSessao(String idUser, String password, String nameEmployee,long idNucleo);

        Empresa pesquisarEmpresaRegistrada();

        void realizarLogin(String user, String password);

        Context getContexto();

        AutenticacaoService getServicoLogin();

        boolean loginValidado();

        void salvarFuncionario(Funcionario funcionario);

        Empresa getEmpresa();

        void setEmpresa(Empresa empresa);

        Nucleo getNucleo();

        void setNucleo(Nucleo nucleo);
    }
    interface  IModel{

        Empresa pesquisarEmpresaRegistrada();

        List<Nucleo> pesquisarTodosNucleos();

        void salvarFuncionario(Funcionario funcionario);
    }
    interface IView {

        void carregarAnimacaoInicializacao();

        boolean validarForm();
    }
}