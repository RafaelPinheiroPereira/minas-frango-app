package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.realm.Preco;
import com.br.minasfrango.data.realm.Produto;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 04717299302 on 28/12/2016.
 */

public class PriceDAO extends GenericsDAO<Preco> {


    public static PriceDAO getInstace(final Class<Preco> type) {
        return new PriceDAO(type);
    }

    public PriceDAO(final Class<Preco> type) {
        super(type);
    }


    public Preco carregaPrecoProduto(Produto produto) {
        Preco preco = new Preco();
        preco.setValor(0.0);

        RealmResults<Preco> result = where().equalTo("chavesPreco.idProduto", produto.getId())
                .equalTo("chavesPreco.unidadeProduto", produto.getUnidade()).findAll();

        if (result != null && result.size() > 0) {
            for (Preco aux : result) {
                if (aux.getValor() != 0) {
                    return aux;
                }
            }
        }

        return preco;
    }

    public Preco findPriceByPriceID(long idPriceID) {
        return idPriceID == 0 ? new Preco() : where().equalTo("chavesPreco.id", idPriceID).findFirst();

    }

    public List<Preco> getAll() {
        List<Preco> preco = new ArrayList<>();
        where().findAll().forEach(item->preco.add(item));
        return preco;
    }
}
