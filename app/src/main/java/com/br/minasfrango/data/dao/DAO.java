package com.br.minasfrango.data.dao;

import io.realm.Realm;
import io.realm.RealmModel;

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
