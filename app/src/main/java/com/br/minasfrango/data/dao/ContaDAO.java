package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.Conta;
import com.br.minasfrango.data.realm.ContaORM;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO extends GenericsDAO<ContaORM> {
    public static ContaDAO getInstace(final Class<ContaORM> type) {
        return new ContaDAO(type);
    }

    public ContaDAO(final Class<ContaORM> type) {
        super(type);
    }

    public List<Conta> obterTodasContas() {
        List<Conta> contas = new ArrayList<>();
        RealmResults<ContaORM> results = where().findAll().sort("id", Sort.DESCENDING);
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
