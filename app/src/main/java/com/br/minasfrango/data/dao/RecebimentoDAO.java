package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.realm.RecebimentoORM;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import java.util.ArrayList;

public class RecebimentoDAO extends GenericsDAO<RecebimentoORM> {

    public static RecebimentoDAO getInstace(final Class<RecebimentoORM> type) {
        return new RecebimentoDAO(type);
    }

    public RecebimentoDAO(final Class<RecebimentoORM> type) {
        super(type);
    }

    public void addRecibo(RecebimentoORM recebimentoORM) {
        try {

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(recebimentoORM);
            realm.commitTransaction();

        } catch (RealmException e) {

            realm.cancelTransaction();
        }
    }

    public Recebimento findByID(Long id) {
        RecebimentoORM result = where().equalTo("id", id).findAll().first();
        if (result != null) {
            return new Recebimento(result);
        }
        return null;
    }

    public ArrayList<Recebimento> pesquisarRecebimentoPorCliente(Cliente cliente) {
        ArrayList<Recebimento> recebimentos = new ArrayList<>();

        RealmResults<RecebimentoORM> results =
                where().equalTo("idCliente", cliente.getId())
                        .and()
                        .isNull("dataRecebimento")
                        .sort("dataVencimento", Sort.ASCENDING)
                        .findAll();

        if (results != null && results.size() > 0) {
            results.forEach(item->recebimentos.add(new Recebimento(item)));
        }

        return recebimentos;
    }
}
