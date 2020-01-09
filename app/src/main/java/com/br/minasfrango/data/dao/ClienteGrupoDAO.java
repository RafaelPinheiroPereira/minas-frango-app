package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.ClienteGrupo;
import com.br.minasfrango.data.realm.ClienteGrupoORM;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

public class ClienteGrupoDAO  extends GenericsDAO<ClienteGrupoORM> {
    public static ClienteGrupoDAO getInstace(final Class<ClienteGrupoORM> type) {
        return new ClienteGrupoDAO(type);
    }

    public ClienteGrupoDAO(final Class<ClienteGrupoORM> type) {
        super(type);
    }
    public List<ClienteGrupo> todos() {
        List<ClienteGrupo> clientesGrupos = new ArrayList<>();
        clientesGrupos.add(new ClienteGrupo(new ClienteGrupoORM(0,"Selecione uma Rede")));
        RealmResults<ClienteGrupoORM> results = where().findAll().sort("nome", Sort.ASCENDING);
        if (VERSION.SDK_INT >= VERSION_CODES.N) {

            results.forEach(item->clientesGrupos.add(new ClienteGrupo(item)));
        } else {
            for (ClienteGrupoORM item : results) {
                clientesGrupos.add(new ClienteGrupo(item));

            }
        }
        return clientesGrupos;
    }
}
