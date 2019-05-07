package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Preco;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.realm.PrecoORM;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 04717299302 on 28/12/2016.
 */

public class PriceDAO extends GenericsDAO<PrecoORM> {

    public static PriceDAO getInstace(final Class<PrecoORM> type) {
        return new PriceDAO(type);
    }

    public PriceDAO(final Class<PrecoORM> type) {
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

    public Preco findPriceByPriceID(long idPriceID) {
        return idPriceID == 0 ? new Preco() : new Preco(where().equalTo("chavesPrecoORM.id", idPriceID).findFirst());

    }

    public List<PrecoORM> getAll() {
        List<PrecoORM> precoORM = new ArrayList<>();
        where().findAll().forEach(item->precoORM.add(item));
        return precoORM;
    }
}
