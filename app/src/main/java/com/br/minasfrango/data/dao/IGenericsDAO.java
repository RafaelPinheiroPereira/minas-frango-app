package com.br.minasfrango.data.dao;

import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.Sort;
import java.util.List;

public interface IGenericsDAO<T extends RealmModel> {

    T copyOrUpdate(T entity);
    T findById(String searchParams,Long id);
    void delete(T persistentEntity);
    RealmQuery<T> where() ;




}
