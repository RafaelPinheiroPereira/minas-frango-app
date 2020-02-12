package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.BlocoRecibo;
import com.br.minasfrango.data.realm.BlocoReciboORM;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

public class BlocoPedidoDAO extends GenericsDAO<BlocoReciboORM> {



    public static BlocoPedidoDAO getInstace(final Class<BlocoReciboORM> type) {
        return new BlocoPedidoDAO(type);
    }


    public BlocoPedidoDAO(final Class<BlocoReciboORM> entity) {
        super(entity);
    }

    public BlocoRecibo consultarBlocoReciboPorNome(final String name) {

        return new BlocoRecibo( where().equalTo("nomeFoto",name).findFirst());
    }

    public List<BlocoRecibo> getRecibosNaoMigrados() {
        List<BlocoRecibo> recibos = new ArrayList<>();
        RealmResults<BlocoReciboORM> results =
                where().equalTo("fotoMigrada", false).findAll();
        if (results.size() > 0 && results != null) {
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                results.forEach(blocoReciboORM->recibos.add(new BlocoRecibo(blocoReciboORM)));
            } else {
                for (BlocoReciboORM blocoReciboORM : results) {
                    recibos.add(new BlocoRecibo(blocoReciboORM));
                }
            }
        }

        return recibos;

    }
}
