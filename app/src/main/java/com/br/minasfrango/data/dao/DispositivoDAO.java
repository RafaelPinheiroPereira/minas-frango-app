package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Dispositivo;
import com.br.minasfrango.data.realm.DispositivoORM;
import io.realm.RealmResults;

public class DispositivoDAO extends  GenericsDAO<DispositivoORM> {
    public static DispositivoDAO getInstace(final Class<DispositivoORM> type) {
        return new DispositivoDAO(type);
    }
    public DispositivoDAO(final Class<DispositivoORM> entity) {
        super(entity);
    }

    public Dispositivo pesquisarAparelhoRegistrado(final long idEmpresa, final String mac) {

        RealmResults<DispositivoORM> realmResults = where().equalTo("idEmpresa",idEmpresa).equalTo("mac",mac).findAll();
        if(realmResults!=null && realmResults.size()>0){
            return  new Dispositivo(realmResults.first());
        }else{return null;}
    }

    public Dispositivo pesquisarDispositivoPorMac(String mac) {
        RealmResults<DispositivoORM> results =
                where().equalTo("mac", mac).findAll();

        if (results.size() > 0 && results != null) {

            return new Dispositivo(  results.first());
        }
        return null;
    }
}
