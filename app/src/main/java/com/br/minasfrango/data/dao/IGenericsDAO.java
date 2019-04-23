package com.br.minasfrango.data.dao;

import io.realm.RealmModel;
import io.realm.RealmQuery;

public interface IGenericsDAO<T extends RealmModel> {

    T copyOrUpdate(T entity);

    T findById(Long id);
    void delete(T persistentEntity);
    RealmQuery<T> where() ;




}
