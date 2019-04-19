package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.Recebimento;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import java.util.ArrayList;

public class RecebimentoDAO extends GenericsDAO<Recebimento> {





    public static RecebimentoDAO getInstace(final Class<Recebimento> type) {
        return new RecebimentoDAO(type);
    }

    public RecebimentoDAO(final Class<Recebimento> type) {
        super(type);
    }

    public Recebimento findByID(Long id) {
        RealmResults<Recebimento> results = where().equalTo("id", id).findAll();
        if (results.size() > 0 && results != null) {
            Recebimento recebimento = new Recebimento(results.get(0).getIdFuncionario(),
                    results.get(0).getIdCliente(), results.get(0).getIdVenda(), results.get(0).getDataVenda(),
                    results.get(0).getValorVenda(), results.get(0).getDataVencimento());
            recebimento.setId(results.get(0).getIdVenda());
            return recebimento;
        }
        return null;
    }


    public ArrayList<Recebimento> findReceiptsByClient(Cliente cliente) {
        ArrayList<Recebimento> recebimentos = new ArrayList<Recebimento>();

        RealmResults<Recebimento> results = where().equalTo("idCliente", cliente.getId())
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
