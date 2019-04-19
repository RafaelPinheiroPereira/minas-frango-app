package com.br.minasfrango.data.dao;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.Sort;
import java.io.Serializable;
import java.util.List;

public class GenericsDAO<T extends RealmModel> implements IGenericsDAO<T> {

    Realm realm;

    Class<T> entity;

    public GenericsDAO(final Class<T> entity) {
        this.entity = entity;
        this.realm = Realm.getDefaultInstance();
    }


    @Override
    public RealmQuery<T> where() {

        return realm.where(entity);

    }

    @Override
    public T copyOrUpdate(T entity) {
        realm.beginTransaction();
        entity = realm.copyToRealmOrUpdate(entity);
        realm.commitTransaction();
        return entity;
    }

    @Override
    public T findById(final String searchParams, final Long id) {

        return where().equalTo(searchParams, id).findFirst();

    }


    @Override
    public void delete(final T persistentEntity) {
        realm.beginTransaction();
        realm.delete(persistentEntity.getClass());
        realm.commitTransaction();
    }




}
