package com.br.minasfrango.ui.mvp.configuracao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.dao.DispositivoDAO;
import com.br.minasfrango.data.dao.EmpresaDAO;
import com.br.minasfrango.data.dao.NucleoDAO;
import com.br.minasfrango.data.model.Configuracao;
import com.br.minasfrango.data.model.Dispositivo;
import com.br.minasfrango.data.model.Empresa;
import com.br.minasfrango.data.model.Nucleo;
import com.br.minasfrango.data.realm.DispositivoORM;
import com.br.minasfrango.data.realm.EmpresaORM;
import com.br.minasfrango.data.realm.NucleoORM;
import com.br.minasfrango.ui.mvp.configuracao.IConfiguracaoMVP.IModel;
import io.realm.Realm;

public class Model implements IModel {

    Presenter mPresenter;
    EmpresaDAO mEmpresaDAO = EmpresaDAO.getInstace(EmpresaORM.class);
    NucleoDAO mNucleoDAO = NucleoDAO.getInstace(NucleoORM.class);
    DispositivoDAO mDispositivoDAO = DispositivoDAO.getInstace(DispositivoORM.class);

    public Model(final Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String statusSistema() {


            Empresa empresa= mEmpresaDAO.pesquisarEmpresaRegistradaNoDispositivo();

            if(empresa!=null){

                if(empresa.getAtivo().equals("inativo")){
                    return "EMPRESA_INATIVADA";
                }else{
                    Dispositivo dispositivo=mDispositivoDAO.pesquisarAparelhoRegistrado(empresa.getId(),mPresenter.getMac());
                    if(dispositivo!=null){
                        if(dispositivo.getAtivo().equals("inativo")){
                            return  "DISPOSITIVO_INATIVADO";
                        }else {
                            return "DISPOSITIVO_HABILITADO";
                        }

                    }else{
                        return "DISPOSITIVO_NAO_CADASTRADO";
                    }
                }

            }else{
                return "EMPRESA NAO CADASTRADA";
            }





    }

    @Override
    public void salvarConfiguracao(final Configuracao configuracao) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new EmpresaORM(configuracao.getEmpresa()));
        realm.commitTransaction();

        realm.beginTransaction();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            configuracao
                    .getEmpresa()
                    .getNucleos()
                    .forEach(
                            nucleo -> {
                                realm.copyToRealmOrUpdate(new NucleoORM(nucleo));
                            });
        } else {
            for (Nucleo nucleo : configuracao.getEmpresa().getNucleos()) {
                realm.copyToRealmOrUpdate(new NucleoORM(nucleo));
            }
        }
        realm.commitTransaction();

        realm.beginTransaction();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {

            configuracao
                    .getEmpresa()
                    .getDispositivos()
                    .forEach(
                            dispositivo -> {
                                realm.copyToRealmOrUpdate(new DispositivoORM(dispositivo));
                            });
        } else {

            for (Dispositivo dispositivo : configuracao.getEmpresa().getDispositivos()) {

                realm.copyToRealmOrUpdate(new DispositivoORM(dispositivo));
            }
        }
        realm.commitTransaction();
    }
}
