package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.TipoRecebimento;
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

    RealmQuery<TipoRecebimento> query;

    private RealmQuery<TipoRecebimento> where() {
        return realm.where(TipoRecebimento.class);
    }

    public static TipoRecebimentoDAO getInstace() {
        return new TipoRecebimentoDAO();
    }

    public TipoRecebimentoDAO() {
        realm = Realm.getDefaultInstance();
    }

    public List<TipoRecebimento> findTipoRecebimentoByCliente(Cliente cliente) {
        List<TipoRecebimento> tipoRecebimentos = new ArrayList<TipoRecebimento>();
        query = realm.where(TipoRecebimento.class);
        RealmResults<TipoRecebimento> results = query.findAll();

        if (results != null && results.size() > 0) {

            for (TipoRecebimento tipoRecebimento : results) {
                tipoRecebimentos.add(transformaResultEmTipoRecebimento(tipoRecebimento));
            }
            return tipoRecebimentos;
        }
        return null;
    }


    private TipoRecebimento transformaResultEmTipoRecebimento(TipoRecebimento tipoRecebimentoToTransforme) {
        TipoRecebimento tipoRecebimento = new TipoRecebimento();
        tipoRecebimento.setId(tipoRecebimentoToTransforme.getId());
        tipoRecebimento.setNome(tipoRecebimentoToTransforme.getNome());

        return tipoRecebimento;
    }


    public ArrayList<String> carregaFormaPagamentoAmortizacao() throws Throwable {
        ArrayList<String> formas = new ArrayList<String>();
        RealmResults<TipoRecebimento> results = where().equalTo("id", 1).findAll();
        Optional.ofNullable(results).orElseThrow(NullPointerException::new);
        results.forEach(item->formas.add(String.valueOf(item.getNome())));
        return formas;
    }

    public TipoRecebimento findById(long id) throws Throwable {
        TipoRecebimento result = where().equalTo("id", id).findAll().first();
        return Optional.ofNullable(transformaResultToTipoRecebimento(result)).orElse(new TipoRecebimento());
    }

    private TipoRecebimento transformaResultToTipoRecebimento(TipoRecebimento result) {
        return new TipoRecebimento(result.getId(), result.getNome());
    }

    public int codigoFormaPagamento(String descricaoFormaPagamento) {

        RealmQuery<TipoRecebimento> formaPagamentoRealmQuery = realm.where(TipoRecebimento.class);
        TipoRecebimento result = formaPagamentoRealmQuery.equalTo("nome", descricaoFormaPagamento)
                .findFirst();
        int codigo = 0;
        if (result != null) {
            codigo = (int) result.getId();
        }
        return codigo;

    }
}
