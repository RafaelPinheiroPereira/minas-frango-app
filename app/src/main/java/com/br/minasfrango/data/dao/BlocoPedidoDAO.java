package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.realm.BlocoReciboORM;

public class BlocoPedidoDAO extends GenericsDAO<BlocoReciboORM> {



    public static BlocoPedidoDAO getInstace(final Class<BlocoReciboORM> type) {
        return new BlocoPedidoDAO(type);
    }


    public BlocoPedidoDAO(final Class<BlocoReciboORM> entity) {
        super(entity);
    }
}
