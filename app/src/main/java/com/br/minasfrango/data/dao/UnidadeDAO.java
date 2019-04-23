package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.realm.Produto;
import com.br.minasfrango.data.realm.Unidade;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 28/12/2016.
 */
public class UnidadeDAO extends GenericsDAO<Unidade> {

    Realm realm;

    RealmResults<Unidade> result;

    Class<Unidade> type;

    public static UnidadeDAO getInstace(Class<Unidade> type) {
        return new UnidadeDAO(type);
    }

    public UnidadeDAO(Class<Unidade> type) {
        super(type);
    }

    public Unidade findUnityPattenByProduct(Produto product) {
        Unidade unity =
                where()
                        .equalTo("chavesUnidade.idProduto", product.getId())
                        .and()
                        .equalTo("unidadePadrao", "S")
                        .findFirst();
        return convertRealmToDTO(unity);
    }

    public List<String> findUnitysOfProductsToString(Produto product) {
        ArrayList<String> strUnidades = new ArrayList<String>();
        RealmResults<Unidade> results =
                where()
                        .equalTo("chavesUnidade.idProduto", product.getId())
                        .and()
                        .notEqualTo("unidadePadrao", "S")
                        .findAll();
        results.forEach(item->strUnidades.add(convertRealmToDTO(item).getNome()));
        return strUnidades;
    }

    public List<Unidade> getAll() {
        List<Unidade> unidades = new ArrayList<>();
        where().findAll().forEach(item->unidades.add(convertRealmToDTO(item)));
        return unidades;
    }

    private Unidade convertRealmToDTO(Unidade unity) {
        return new Unidade(
                unity.getId(), unity.getChavesUnidade(), unity.getNome(), unity.getUnidadePadrao());
    }
}
