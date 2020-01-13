package com.br.minasfrango.data.dao;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.ItemPedidoORM;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 13/01/2017.
 */
public class ItemPedidoDAO extends GenericsDAO<ItemPedidoORM> {

    public static ItemPedidoDAO getInstace(final Class<ItemPedidoORM> type) {
        return new ItemPedidoDAO(type);
    }

    public ItemPedidoDAO(final Class<ItemPedidoORM> type) {
        super(type);
    }

    public long addItemPedido(ItemPedido itemPedido) {

        ItemPedidoORM itemPedidoORM = new ItemPedidoORM(itemPedido);


        long id;
        if (realm.where(ItemPedidoORM.class).max("id") != null) {
            id = (long) (realm.where(ItemPedidoORM.class).max("id").intValue() + 1);
        } else {
            id = 1;
        }

        try {
            itemPedidoORM.setId(id);
            itemPedido.setId(id);
            itemPedidoORM.setChavesItemPedidoORM(itemPedidoORM.getChavesItemPedidoORM());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(itemPedidoORM);
            realm.commitTransaction();

        } catch (IOException e) {
            realm.cancelTransaction();
        }
        return id;
    }


    public List<ItemPedido> allItensByPedido(Pedido pedido) {
        List<ItemPedido> itens = new ArrayList<>();




        RealmResults<ItemPedidoORM> results =
                where().equalTo("chavesItemPedidoORM.idVenda", Double.valueOf(pedido.getIdVenda()))
                        .findAll();
        if (results.size() > 0 && results != null) {

            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                results.forEach(itemPedidoORM->itens.add(new ItemPedido(itemPedidoORM)));
            } else {
                for (ItemPedidoORM itemPedidoORM : results) {

                    itens.add(new ItemPedido(itemPedidoORM));
                }
            }

            return itens;
        }
        return null;
    }

    public void removeItemPedido(ItemPedidoORM itemPedidoORM) {

        RealmResults<ItemPedidoORM> result =
                realm.where(ItemPedidoORM.class).equalTo("id", itemPedidoORM.getId()).findAll();

        if (result.size() > 0 && result != null) {
            try {

                realm.beginTransaction();
                result.deleteAllFromRealm();
                realm.commitTransaction();

            } catch (IOException e) {
                realm.cancelTransaction();
            }
        }
    }

    public ItemPedido searchItem(ItemPedido item) {

        RealmQuery<ItemPedidoORM> query = realm.where(ItemPedidoORM.class);
        ItemPedidoORM result = query.equalTo("id", item.getId()).findAll().first();
        if (result != null) {
            return new ItemPedido(result);
        }
        return null;
    }

    public void updateItem(ItemPedidoORM itemPedidoORM) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(itemPedidoORM);
        realm.commitTransaction();
    }
}
