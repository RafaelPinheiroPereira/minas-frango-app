package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.realm.PrecoIDORM;
import io.realm.RealmResults;
import io.realm.Sort;

public class PrecoIDDAO extends GenericsDAO<PrecoIDORM> {

    public static PrecoIDDAO getInstance(Class<PrecoIDORM> entity) {
        return new PrecoIDDAO(entity);
    }

    public PrecoIDDAO(final Class<PrecoIDORM> entity) {
        super(entity);
    }

    public String findPrecoIDByUnidadeAndProdutoAndCliente(Produto produto, String unidade, Cliente cLiente) {

        RealmResults<PrecoIDORM> result = where().equalTo("idProduto", produto.getId()).and()
                .equalTo("unidadeProduto", unidade).and()
                .equalTo("idCliente", Double.parseDouble(String.valueOf(cLiente.getId()))).findAll();

        return result.size() == 0 ? "" : result.first().getId();

    }

    public String findPrecoIDByUnidadeAndProdutoPadrao(Produto produto, String unidade) {

        RealmResults<PrecoIDORM> result = where().equalTo("idProduto", produto.getId()).and()
                .equalTo("unidadeProduto", unidade).equalTo("idCliente",0.0).sort("dataPreco", Sort.DESCENDING)
               .findAll();

        return result.size() == 0 ? "" : result.first().getId();

    }


}
