package com.br.minasfrango.dao;

import com.br.minasfrango.model.Preco;
import com.br.minasfrango.model.Produto;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


/**
 * Created by 04717299302 on 28/12/2016.
 */

public class PrecoDAO extends DAO<Preco> {



    RealmQuery<Preco> where() {
        return realm.where(Preco.class);
    }

    public static PrecoDAO getInstace() {
        return new PrecoDAO();
    }

    public PrecoDAO() {
        super();
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
