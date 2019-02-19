package com.br.minasfrango.dao;

import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.ItemPedidoID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.IOException;


/**
 * Created by 04717299302 on 13/01/2017.
 */

public class ItemPedidoDAO {

    Realm realm ;

    public static  ItemPedidoDAO getInstace(){
        return new ItemPedidoDAO();
    }
    public ItemPedidoDAO() {
        realm=Realm.getDefaultInstance();
    }

    public  long addItemPedido(ItemPedido itemPedido){

        long idItemPedidoId;
        if(realm.where(ItemPedidoID.class).max("id")!=null){
            idItemPedidoId =  (long) (realm.where(ItemPedidoID.class).max("id").intValue() +1);
        }else{
            idItemPedidoId=1;
        }
        
        itemPedido.getChavesItemPedido().setId(idItemPedidoId);
    
    
        long id;
        if(realm.where(ItemPedido.class).max("id")!=null){
            id =  (long) (realm.where(ItemPedido.class).max("id").intValue() +1);
        }else{
            id=1;
        }
        


        try {
            itemPedido.setId(id);

            realm.beginTransaction();
            realm.copyToRealm(itemPedido);
            realm.commitTransaction();

        }catch (IOException e){
             realm.cancelTransaction();
        }
return  id;
    }

    public  void removeItemPedido(ItemPedido itemPedido){


        RealmResults<ItemPedido> result = realm.where(ItemPedido.class).equalTo("id",itemPedido.getId()).findAll();


        if(result.size()>0 && result!=null) {
            try {


                realm.beginTransaction();
                result.deleteAllFromRealm();
                realm.commitTransaction();

            } catch (IOException e) {
                realm.cancelTransaction();
            }
        }

    }
    public  ItemPedido searchItem(ItemPedido item){
         ItemPedido itemPedido = new ItemPedido();
        RealmQuery<ItemPedido> query = realm.where(ItemPedido.class);
        RealmResults<ItemPedido> itemPedidoRealmResults = query.equalTo("id",item.getId()).findAll();
        if(itemPedidoRealmResults.size()>0 && itemPedidoRealmResults!=null){
            ItemPedidoID itemPedidoID= new ItemPedidoID();
            itemPedidoID.setId(itemPedidoRealmResults.get(0).getChavesItemPedido().getId());
            itemPedidoID.setIdUnidade(itemPedidoRealmResults.get(0).getChavesItemPedido().getIdUnidade());
            itemPedidoID.setIdProduto(itemPedidoRealmResults.get(0).getChavesItemPedido().getIdProduto());
            itemPedido.setChavesItemPedido(itemPedidoID);
            itemPedido.setId(itemPedidoRealmResults.get(0).getId());
            itemPedido.setDescricao(itemPedidoRealmResults.get(0).getDescricao());
            itemPedido.setValorUnitario(itemPedidoRealmResults.get(0).getValorUnitario());
            itemPedido.setQuantidade(itemPedidoRealmResults.get(0).getQuantidade());
                  }
        return itemPedido;
    }

    public void updateItem(ItemPedido itemPedido) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(itemPedido);
        realm.commitTransaction();
    }
}
