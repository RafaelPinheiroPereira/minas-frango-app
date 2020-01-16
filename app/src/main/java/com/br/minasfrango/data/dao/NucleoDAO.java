package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.realm.NucleoORM;

public class NucleoDAO extends GenericsDAO<NucleoORM> {

    public static NucleoDAO getInstace(final Class<NucleoORM> type) {
        return new NucleoDAO(type);
    }

    public NucleoDAO(final Class<NucleoORM> entity) {
        super(entity);
    }
}
