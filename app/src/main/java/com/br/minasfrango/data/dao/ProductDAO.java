package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.Produto;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 26/12/2016.
 */

public class ProductDAO extends GenericsDAO<Produto> {


    public static ProductDAO getInstace(final Class<Produto> type) {
        return new ProductDAO(type);
    }

    public ProductDAO(final Class<Produto> type) {
        super(type);
    }

    public Produto findByName(final String productName) {
        return convertRealmToPojo(where().equalTo("nome", productName).findFirst());
    }

    public List<Produto> getAll() {
        List<Produto> produtos = new ArrayList<>();
        where().findAll().sort("nome", Sort.ASCENDING).forEach(item->produtos.add(convertRealmToPojo(item)));
        return produtos;

    }

    //Converter realm em DTO
    private Produto convertRealmToPojo(final Produto item) {
        return new Produto(item.getId(), item.getNome(), item.getUnidade(), item.getQuantidade());
    }


}
