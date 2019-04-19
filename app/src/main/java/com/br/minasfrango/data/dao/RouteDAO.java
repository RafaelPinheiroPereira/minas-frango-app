package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.Rota;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO extends GenericsDAO<Rota> {


    public static RouteDAO getInstace(final Class<Rota> type) {
        return new RouteDAO(type);
    }
    public RouteDAO(final Class<Rota> type) {
        super(type);
    }
    public List<Rota> allRoutes() {
        List<Rota> rotas = new ArrayList<Rota>();
        RealmResults<Rota> results = where().findAll();
        results.forEach(item -> rotas.add(new Rota(item.getId(), item.getFuncionario(), item.getNome())));
        return rotas;
    }
}
