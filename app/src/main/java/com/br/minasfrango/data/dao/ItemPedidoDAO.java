package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.ItemPedido;
import com.br.minasfrango.data.pojo.ItemPedidoID;
import com.br.minasfrango.data.pojo.Pedido;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 04717299302 on 13/01/2017.
 */

public class ItemPedidoDAO  extends  DAO<ItemPedido>{



    private RealmQuery<ItemPedido> where() {
        return realm.where(ItemPedido.class);
    }

    public static  ItemPedidoDAO getInstace(){
        return new ItemPedidoDAO();
    }
    public ItemPedidoDAO() {
        super();
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

        RealmQuery<ItemPedido> query = realm.where(ItemPedido.class);
        RealmResults<ItemPedido> itemPedidoRealmResults = query.equalTo("id",item.getId()).findAll();
        if(itemPedidoRealmResults.size()>0 && itemPedidoRealmResults!=null){
                 return transformaResultItemPedido(itemPedidoRealmResults.first());
                  }
                  return null;

    }

    public void updateItem(ItemPedido itemPedido) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(itemPedido);
        realm.commitTransaction();
    }

    public List<ItemPedido> allItensByPedido(Pedido pedido){
        List<ItemPedido> itens= new ArrayList<>();
        RealmResults<ItemPedido> results = where().equalTo("chavesItemPedido.idVenda",Double.valueOf(pedido.getId())).findAll();
        if(results.size()>0 && results!=null){
            for(ItemPedido result:results){
                itens.add(transformaResultItemPedido(result));
            }
            return itens;
        }
        return null;
    }

    private ItemPedido transformaResultItemPedido(final ItemPedido result) {
        ItemPedido itemPedido = new ItemPedido();
        ItemPedidoID itemPedidoID= new ItemPedidoID();
        itemPedidoID.setId(result.getChavesItemPedido().getId());
        itemPedidoID.setIdUnidade(result.getChavesItemPedido().getIdUnidade());
        itemPedidoID.setIdProduto(result.getChavesItemPedido().getIdProduto());
        itemPedido.setChavesItemPedido(itemPedidoID);
        itemPedido.setId(result.getId());
        itemPedido.setDescricao(result.getDescricao());
        itemPedido.setValorUnitario(result.getValorUnitario());
        itemPedido.setQuantidade(result.getQuantidade());
        itemPedido.setValorTotal(result.getValorTotal());
        return itemPedido;
    }
}
