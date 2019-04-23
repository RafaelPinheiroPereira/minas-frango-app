package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.PrecoID;
import com.br.minasfrango.data.realm.Produto;
import io.realm.RealmResults;

public class PrecoIDDAO extends GenericsDAO<PrecoID> {

    public static PrecoIDDAO getInstance(Class<PrecoID> entity) {
        return new PrecoIDDAO(entity);
    }

    public PrecoIDDAO(final Class<PrecoID> entity) {
        super(entity);
    }

    public long findPrecoIDByUnidadeAndProdutoAndCliente(Produto produto, String unidade, Cliente cLiente) {

        RealmResults<PrecoID> result = where().equalTo("idProduto", produto.getId()).and()
                .equalTo("unidadeProduto", unidade).and()
                .equalTo("idCliente", Double.parseDouble(String.valueOf(cLiente.getId()))).findAll();

        return result.size() == 0 ? 0 : result.first().getId();

    }


}
