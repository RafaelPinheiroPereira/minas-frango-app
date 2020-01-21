package com.br.minasfrango.ui.mvp.login;

import com.br.minasfrango.data.dao.EmpresaDAO;
import com.br.minasfrango.data.dao.FuncionarioDAO;
import com.br.minasfrango.data.dao.NucleoDAO;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.model.Nucleo;
import com.br.minasfrango.data.realm.EmpresaORM;
import com.br.minasfrango.data.realm.FuncionarioORM;
import com.br.minasfrango.data.realm.NucleoORM;
import com.br.minasfrango.ui.mvp.login.ILoginMVP.IModel;
import io.realm.Realm;
import java.util.List;

public class Model implements IModel {
    private Presenter mPresenter;

    private FuncionarioDAO mFuncionarioDAO = FuncionarioDAO.getInstace(FuncionarioORM.class);
    private NucleoDAO mNucleoDAO = NucleoDAO.getInstace(NucleoORM.class);
    private EmpresaDAO mEmpresaDAO= EmpresaDAO.getInstace(EmpresaORM.class);


    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Empresa pesquisarEmpresaRegistrada() {
        return mEmpresaDAO.pesquisarEmpresaRegistradaNoDispositivo();
    }

    @Override
    public List<Nucleo> pesquisarTodosNucleos() {
        return mNucleoDAO.getAllNucleos();
    }

    @Override
    public void salvarFuncionario(final Funcionario funcionario) {
        FuncionarioORM funcionarioORM= new FuncionarioORM(funcionario);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(funcionarioORM);
        realm.commitTransaction();

    }
}
