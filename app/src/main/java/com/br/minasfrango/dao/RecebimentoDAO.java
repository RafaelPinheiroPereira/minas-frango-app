package com.br.minasfrango.dao;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Recebimento;

import io.realm.RealmQuery;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import java.util.Iterator;

public class RecebimentoDAO {

    Realm realm;

    Recebimento recebimento;

    public static RecebimentoDAO getInstace() {
        return new RecebimentoDAO();
    }

    public RecebimentoDAO() {
        realm = Realm.getDefaultInstance();
    }


    public Recebimento findByID(Long id) {

        RealmResults<Recebimento> results = realm.where(Recebimento.class).equalTo("id", id).findAll();

        if (results.size() > 0 && results != null) {
            Recebimento recebimento = new Recebimento(results.get(0).getIdFuncionario(),
                    results.get(0).getIdCliente(), results.get(0).getIdVenda(), results.get(0).getDataVenda(),
                    results.get(0).getValorVenda(), results.get(0).getDataVencimento());
            recebimento.setId(results.get(0).getIdVenda());
            return recebimento;
        }
        return null;
    }


    public ArrayList<Recebimento> recebimentosPorCliente(Cliente cliente) {
        ArrayList<Recebimento> recebimentos = new ArrayList<Recebimento>();

        RealmResults<Recebimento> results = realm.where(Recebimento.class).equalTo("idCliente", cliente.getId())
                .findAll();

        if (results != null && results.size() > 0) {

            results = results.sort("dataVencimento", Sort.ASCENDING);

            for (int i = 0; i < results.size(); i++) {

                Recebimento recebimento = new Recebimento();
                recebimento.setId(results.get(i).getId());
                recebimento.setIdCliente(results.get(i).getIdCliente());
                recebimento.setIdFuncionario(results.get(i).getIdFuncionario());
                recebimento.setValorVenda(results.get(i).getValorVenda());
                recebimento.setIdVenda(results.get(i).getIdVenda());
                recebimento.setDataVenda(results.get(i).getDataVenda());
                recebimento.setDataVencimento(results.get(i).getDataVencimento());
                recebimento.setValorAmortizado(results.get(i).getValorAmortizado());
                recebimentos.add(recebimento);
            }

        }

        return recebimentos;

    }

    public void updateRecebimento(Recebimento recebimento) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(recebimento);
        realm.commitTransaction();
    }


    public void addRecibo(Recebimento recebimento) {
        try {

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(recebimento);
            realm.commitTransaction();

        } catch (RealmException e) {

            realm.cancelTransaction();

        }

    }

}
