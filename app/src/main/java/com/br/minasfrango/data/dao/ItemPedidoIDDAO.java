package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.model.ItemPedidoID;
import com.br.minasfrango.data.realm.ItemPedidoIDORM;

public class ItemPedidoIDDAO extends GenericsDAO<ItemPedidoIDORM> {

    public static ItemPedidoIDDAO getInstace(final Class<ItemPedidoIDORM> type) {
        return new ItemPedidoIDDAO(type);
    }

    public ItemPedidoIDDAO(final Class<ItemPedidoIDORM> type) {
        super(type);
    }

    public void inserir(final ItemPedidoID itemPedidoID) {
        long idItemPedidoId;
        ItemPedidoIDORM itemPedidoIDORM = new ItemPedidoIDORM(itemPedidoID);
        if (realm.where(ItemPedidoIDORM.class).max("id") != null) {
            idItemPedidoId = (long) (realm.where(ItemPedidoIDORM.class).max("id").intValue() + 1);
        } else {
            idItemPedidoId = 1;
        }
        itemPedidoIDORM.setId(idItemPedidoId);
        itemPedidoID.setId(idItemPedidoId);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(itemPedidoIDORM);
        realm.commitTransaction();

    }


}
