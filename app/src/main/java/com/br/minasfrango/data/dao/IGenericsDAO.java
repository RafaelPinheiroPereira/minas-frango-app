package com.br.minasfrango.data.dao;

import io.realm.RealmModel;
import io.realm.RealmQuery;

public interface IGenericsDAO<T extends RealmModel> {

    T alterar(T entity);
    T inserir(T entity);

    T findById(Long id);
    void deletar(T persistentEntity);
    RealmQuery<T> where() ;




}
