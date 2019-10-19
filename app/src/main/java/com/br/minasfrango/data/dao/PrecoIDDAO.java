package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Produto;
import com.br.minasfrango.data.realm.PrecoIDORM;
import io.realm.RealmResults;
import java.text.NumberFormat;

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


}
