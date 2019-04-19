package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.Preco;
import com.br.minasfrango.data.pojo.Produto;
import io.realm.RealmResults;


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

    public Preco carregaPrecoUnidadeProduto(Produto produto, String unidade) {
        Preco preco = new Preco();
        preco.setValor(0);

        RealmResults<Preco> result = where().equalTo("chavesPreco.idProduto", produto.getId())
                .equalTo("chavesPreco.unidadeProduto", unidade).findAll();

        if (result != null && result.size() > 0) {
            for (Preco aux : result) {
                if (aux.getValor() != 0) {
                    return aux;
                }

            }
        }

        return preco;
    }
}
