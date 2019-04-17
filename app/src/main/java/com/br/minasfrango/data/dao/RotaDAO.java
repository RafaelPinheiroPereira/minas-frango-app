package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.Rota;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.ArrayList;

public class RotaDAO  extends DAO<Rota>{





    public static RotaDAO getInstace() {
        return new RotaDAO();
    }

    public RotaDAO() {
        super();
    }

    private RealmQuery<Rota> where() {
        return realm.where(Rota.class);
    }

    public ArrayList<Rota> allRoutes() {
        ArrayList<Rota> rotas = new ArrayList<Rota>();
        RealmResults<Rota>  result = where().findAll();
        if (result != null) {

            for (int i = 0; i < result.size(); i++) {
                Rota rota = new Rota(result.get(i).getId(), result.get(i).getFuncionario(), result.get(i).getNome());
                rotas.add(rota);
            }
        }
        return rotas;

    }
}
