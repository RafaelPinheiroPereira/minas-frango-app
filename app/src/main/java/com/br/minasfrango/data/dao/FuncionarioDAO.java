package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Funcionario;
import com.br.minasfrango.data.realm.FuncionarioORM;

import io.realm.RealmResults;

public class FuncionarioDAO extends GenericsDAO<FuncionarioORM> {
    public static FuncionarioDAO getInstace(final Class<FuncionarioORM> type) {
        return new FuncionarioDAO(type);
    }
    public FuncionarioDAO(final Class<FuncionarioORM> entity) {
        super(entity);
    }

    public Funcionario pesquisarPorId(long id){
        FuncionarioORM realmResults= where().equalTo("id",id).findAll().first();
        if(realmResults!=null){
            return new Funcionario(realmResults);
        }else{
            return new Funcionario();
        }
    }
}
