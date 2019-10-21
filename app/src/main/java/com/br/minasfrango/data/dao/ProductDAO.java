package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.realm.ProdutoORM;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 26/12/2016.
 */

public class ProductDAO extends GenericsDAO<ProdutoORM> {

    public static ProductDAO getInstace(final Class<ProdutoORM> type) {
        return new ProductDAO(type);
    }

    public ProductDAO(final Class<ProdutoORM> type) {
        super(type);
    }

    public Produto findByName(final String productName) {
        return new Produto(where().equalTo("nome", productName).findFirst());
    }


    public List<Produto> getAll() {
        List<Produto> produtos = new ArrayList<>();
        RealmResults<ProdutoORM> results = where().findAll().sort("nome", Sort.ASCENDING);
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            results.forEach(produtoORM->produtos.add(new Produto(produtoORM)));
        } else {
            for (ProdutoORM produtoORM : results) {
                produtos.add(new Produto(produtoORM));
            }
        }
        return produtos;

    }

    //Converter realm em DTO
    private ProdutoORM convertRealmToPojo(final ProdutoORM item) {
        return new ProdutoORM(item.getId(), item.getNome(), item.getUnidade(), item.getQuantidade());
    }


}
