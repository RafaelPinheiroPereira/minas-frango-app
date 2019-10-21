package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.model.Unidade;
import com.br.minasfrango.data.realm.ProdutoORM;
import com.br.minasfrango.data.realm.UnidadeORM;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 28/12/2016.
 */
public class UnidadeDAO extends GenericsDAO<UnidadeORM> {

    Realm realm;

    RealmResults<UnidadeORM> result;

    Class<UnidadeORM> type;

    public static UnidadeDAO getInstace(Class<UnidadeORM> type) {
        return new UnidadeDAO(type);
    }

    public UnidadeDAO(Class<UnidadeORM> type) {
        super(type);
    }

    public Unidade findUnityPattenByProduct(Produto product) {
        UnidadeORM unity =
                where()
                        .equalTo("chavesUnidadeORM.idProduto", product.getId())
                        .and()
                        .equalTo("unidadePadrao", "S")
                        .findFirst();
        return new Unidade(unity);
    }


    public List<String> findUnitysOfProductsToString(ProdutoORM product) {
        ArrayList<String> strUnidades = new ArrayList<String>();
        RealmResults<UnidadeORM> results =
                where()
                        .equalTo("chavesUnidade.idProduto", product.getId())
                        .and()
                        .notEqualTo("unidadePadrao", "S")
                        .findAll();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            results.forEach(item->strUnidades.add(new Unidade(item).getNome()));
        } else {
            for (UnidadeORM unidadeORM : results) {
                strUnidades.add(new Unidade(unidadeORM).getNome());
            }
        }
        return strUnidades;
    }

    public List<Unidade> getAll() {
        List<Unidade> unidade = new ArrayList<>();
        RealmResults<UnidadeORM> results = where().findAll();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            results.forEach(item->unidade.add(new Unidade(item)));
        } else {
            for (UnidadeORM unidadeORM : results) {
                unidade.add(new Unidade(unidadeORM));
            }

        }
        return unidade;
    }


}
