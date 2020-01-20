package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.realm.RecebimentoORM;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import java.util.ArrayList;
import java.util.List;

public class RecebimentoDAO extends GenericsDAO<RecebimentoORM> {

    public static RecebimentoDAO getInstace(final Class<RecebimentoORM> type) {
        return new RecebimentoDAO(type);
    }

    public RecebimentoDAO(final Class<RecebimentoORM> type) {
        super(type);
    }

    public long addRecibo(RecebimentoORM recebimentoORM) {
        long id=0;
        try {



            if (this.existeReciboComIdMaiorDoQueOCodigoDeReciboMaximo(recebimentoORM.getId())) {
                id = where().max("id").longValue() +1;

            }
            else{
                if (recebimentoORM.getId()>0) {
                    id = recebimentoORM.getId() + 1;
                } else {
                    id = 1;
                }
            }

            recebimentoORM.setId(id);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(recebimentoORM);
            realm.commitTransaction();

        } catch (RealmException e) {

            realm.cancelTransaction();
        }

        return  id;
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
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                results.forEach(item->recebimentos.add(new Recebimento(item)));
            } else {
                for (RecebimentoORM recebimentoORM : results) {
                    recebimentos.add(new Recebimento(recebimentoORM));

                }
            }
        }

        return recebimentos;
    }

    public List<Recebimento> pesquisarTodosRecebimentos() {

        ArrayList<Recebimento> recebimentos = new ArrayList<>();

        RealmResults<RecebimentoORM> results =
                where().equalTo("check", true)
                        .findAll();

        if (results != null && results.size() > 0) {
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                results.forEach(item->recebimentos.add(new Recebimento(item)));
            } else {
                for (RecebimentoORM recebimentoORM : results) {
                    recebimentos.add(new Recebimento(recebimentoORM));

                }
            }
        }

        return recebimentos;
    }


    public boolean existeReciboComIdMaiorDoQueOCodigoDeReciboMaximo(long codigoReciboMaximo){
        long id;
        if (where().max("id") != null) {
            id = where().max("id").longValue();
            return id > codigoReciboMaximo;
        }
        else {
            return false;
        }
    }
}
