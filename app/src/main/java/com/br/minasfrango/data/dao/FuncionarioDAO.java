package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.realm.FuncionarioORM;

public class FuncionarioDAO extends GenericsDAO<FuncionarioORM> {
    public static FuncionarioDAO getInstace(final Class<FuncionarioORM> type) {
        return new FuncionarioDAO(type);
    }
    public FuncionarioDAO(final Class<FuncionarioORM> entity) {
        super(entity);
    }
}
