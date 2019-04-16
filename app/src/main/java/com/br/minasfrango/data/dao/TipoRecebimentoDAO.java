package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.TipoRecebimento;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 16/01/2017.
 */

public class TipoRecebimentoDAO {


    Realm realm;

    RealmQuery<TipoRecebimento> query;

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


    public int codigoFormaPagamento(String descricaoFormaPagamento) {

        RealmQuery<TipoRecebimento> formaPagamentoRealmQuery = realm.where(TipoRecebimento.class);
        RealmResults<TipoRecebimento> result = formaPagamentoRealmQuery.equalTo("nome", descricaoFormaPagamento)
                .findAll();
        int codigo = 0;
        if (result != null) {

            for (TipoRecebimento tipoRecebimento : result) {
                codigo = (int) tipoRecebimento.getId();
            }
        }
        return codigo;

    }

    public TipoRecebimento findById(long id) {

        query = realm.where(TipoRecebimento.class);
        TipoRecebimento result = query.equalTo("id", id).findAll().first();

        if (result != null) {

            return transformaResultToTipoRecebimento(result);
        }
        return null;

    }

    private TipoRecebimento transformaResultToTipoRecebimento(TipoRecebimento result) {
        return new TipoRecebimento(result.getId(), result.getNome());
    }

    public ArrayList<String> carregaFormaPagamentoAmortizacao() {
        ArrayList<String> formas = new ArrayList<String>();
        RealmQuery<TipoRecebimento> formaPagamentoRealmQuery = realm.where(TipoRecebimento.class);
        RealmResults<TipoRecebimento> result = formaPagamentoRealmQuery.equalTo("id", 1).findAll();

        if (result != null) {

            for (TipoRecebimento tipoRecebimento : result) {
                formas.add(String.valueOf(tipoRecebimento.getNome()));
            }
        }
        return formas;
    }
}
