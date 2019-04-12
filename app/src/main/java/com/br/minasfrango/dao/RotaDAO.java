package com.br.minasfrango.dao;

import com.br.minasfrango.model.Rota;

import io.realm.RealmQuery;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

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

    public ArrayList<Rota> carregaRota() {
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
