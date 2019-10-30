package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Conta;
import com.br.minasfrango.data.realm.ContaORM;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO extends GenericsDAO<ContaORM> {
    public static ContaDAO getInstace(final Class<ContaORM> type) {
        return new ContaDAO(type);
    }

    public ContaDAO(final Class<ContaORM> type) {
        super(type);
    }

    public List<Conta> pesquisarContaPorId() {
        List<Conta> contas = new ArrayList<>();
        RealmResults<ContaORM> results = where().equalTo("descricao","DINHEIRO").findAll();
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            results.forEach(contaORM->contas.add(new Conta(contaORM)));
        } else {
            for (ContaORM contaORM : results) {
                contas.add(new Conta(contaORM));
            }
        }
        return contas;
    }
}
