package com.br.minasfrango.dao;

import com.br.minasfrango.model.Cliente;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmQuery;

public abstract class DAO<T extends RealmModel> {

    Realm realm;

    public DAO() {
        realm = Realm.getDefaultInstance();
    }

    public  T copyOrUpdate(T entity){
        realm.beginTransaction();
        entity=realm.copyToRealmOrUpdate(entity);
        realm.commitTransaction();
        return entity;
    }



}
