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



        Call<Funcionario> autenticarLogin(long idUser, String password,long idEmpresa);

        List<Nucleo> carregarTodosOsNucleos();

        void criarSessao(long idUser, String password, String nameEmployee, long idNucleo, long maxIdVenda,
                final long maxIdRecibo);

        Empresa pesquisarEmpresaRegistrada();

        void realizarLogin(long user, String password);

        Context getContexto();

        AutenticacaoService getServicoLogin();

        boolean estaoValidadosOsInputs();

        void salvarFuncionario(Funcionario funcionario);

        Empresa getEmpresa();

        void setEmpresa(Empresa empresa);

        Nucleo getNucleo();

        void setNucleo(Nucleo nucleo);

        long getIdUsuario();

        void setIdUsuario(long idUsuario);

        String getSenha();

        void setSenha(String senha);

        void solicitarLoginGoogleDrive();
    }
    interface  IModel{

        Empresa pesquisarEmpresaRegistrada();

        List<Nucleo> pesquisarTodosNucleos();

        void salvarFuncionario(Funcionario funcionario);
    }
    interface IView {

        void carregarAnimacaoInicializacao();

        void solicitarLoginGoogleDrive();

        boolean validarForm();
    }
}