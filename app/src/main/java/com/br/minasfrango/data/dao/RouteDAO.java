package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Rota;
import com.br.minasfrango.data.realm.RotaORM;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO extends GenericsDAO<RotaORM> {

    public static RouteDAO getInstace(final Class<RotaORM> type) {
        return new RouteDAO(type);
    }

    public RouteDAO(final Class<RotaORM> type) {
        super(type);
    }


    public List<Rota> todos() {
        List<Rota> rotas = new ArrayList<>();
        RealmResults<RotaORM> results = where().findAll();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            results.forEach(item->rotas.add(new Rota(item)));
        } else {
            for (RotaORM item : results) {
                rotas.add(new Rota(item));

            }
        }
        return rotas;
    }
}
