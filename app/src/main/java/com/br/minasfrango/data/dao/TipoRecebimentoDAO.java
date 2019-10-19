package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.TipoRecebimento;
import com.br.minasfrango.data.realm.TipoRecebimentoORM;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by 04717299302 on 16/01/2017.
 */
public class TipoRecebimentoDAO {

    Realm realm;

    RealmQuery<TipoRecebimentoORM> query;

    public static TipoRecebimentoDAO getInstace() {
        return new TipoRecebimentoDAO();
    }

    public TipoRecebimentoDAO() {
        realm = Realm.getDefaultInstance();
    }

    public ArrayList<String> pesquisarTipoRecebimentoPorId() throws Throwable {
        ArrayList<String> formas = new ArrayList<String>();
        RealmResults<TipoRecebimentoORM> results = where().equalTo("id", 1).findAll();
        Optional.ofNullable(results).orElseThrow(NullPointerException::new);
        results.forEach(item->formas.add(String.valueOf(item.getNome())));
        return formas;
    }

    public int codigoFormaPagamento(String descricaoFormaPagamento) {

        RealmQuery<TipoRecebimentoORM> formaPagamentoRealmQuery =
                realm.where(TipoRecebimentoORM.class);
        TipoRecebimentoORM result =
                formaPagamentoRealmQuery.equalTo("nome", descricaoFormaPagamento).findFirst();
        int codigo = 0;
        if (result != null) {
            codigo = (int) result.getId();
        }
        return codigo;
    }

    public TipoRecebimento findById(long id) throws Throwable {
        TipoRecebimentoORM result = where().equalTo("id", id).findAll().first();
        return Optional.ofNullable(new TipoRecebimento(result)).orElseThrow(Throwable::new);
    }

    public List<TipoRecebimento> findTipoRecebimentoByCliente(Cliente cliente) {
        List<TipoRecebimento> tipoRecebimento = new ArrayList<TipoRecebimento>();
        query = realm.where(TipoRecebimentoORM.class);
        RealmResults<TipoRecebimentoORM> results = query.findAll();

        if (results != null && results.size() > 0) {

            for (TipoRecebimentoORM tipoRecebimentoORM : results) {
                tipoRecebimento.add(new TipoRecebimento(tipoRecebimentoORM));
            }
            return tipoRecebimento;
        }
        return null;
    }

    private TipoRecebimentoORM transformaResultToTipoRecebimento(TipoRecebimentoORM result) {
        return new TipoRecebimentoORM(result.getId(), result.getNome());
    }

    private RealmQuery<TipoRecebimentoORM> where() {
        return realm.where(TipoRecebimentoORM.class);
    }
}
