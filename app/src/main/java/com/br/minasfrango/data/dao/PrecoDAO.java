package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.realm.PrecoORM;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 04717299302 on 28/12/2016.
 */

public class PrecoDAO extends GenericsDAO<PrecoORM> {

    public static PrecoDAO getInstace(final Class<PrecoORM> type) {
        return new PrecoDAO(type);
    }

    public PrecoDAO(final Class<PrecoORM> type) {
        super(type);
    }


    public Preco carregaPrecoProduto(Produto produto) {
        Preco preco = new Preco();
        preco.setValor(0.0);

        RealmResults<PrecoORM> result = where().equalTo("chavesPrecoORM.idProduto", produto.getId())
                .equalTo("chavesPrecoORM.unidadeProduto", produto.getUnidade()).findAll();

        if (result != null && result.size() > 0) {
            for (PrecoORM aux : result) {
                if (aux.getValor() != 0) {
                    return new Preco(aux);
                }
            }
        }

        return preco;
    }

    public Preco findPriceByPriceID(String idPriceID) {

        Preco preco = new Preco();


        RealmResults<PrecoORM> result = where().equalTo("chavesPrecoORM.id", idPriceID).findAll();

        if (result != null && result.size() > 0) {
           return  new Preco(result.first());

        }

        return preco;

    }


    public List<PrecoORM> getAll() {
        List<PrecoORM> precoORM = new ArrayList<>();
        RealmResults<PrecoORM> results = where().findAll();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            results.forEach(item->precoORM.add(item));
        } else {
            for (PrecoORM item : results) {
                precoORM.add(item);
            }

        }
        return precoORM;
    }
}
