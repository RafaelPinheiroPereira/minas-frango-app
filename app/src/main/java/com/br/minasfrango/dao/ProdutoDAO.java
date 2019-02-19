package com.br.minasfrango.dao;

import com.br.minasfrango.model.Produto;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 04717299302 on 26/12/2016.
 */

public class ProdutoDAO {

    Realm realm ;

    public static  ProdutoDAO getInstace(){
        return new ProdutoDAO();
    }
    public ProdutoDAO()
    {
        realm=Realm.getDefaultInstance();
    }

    public RealmResults<Produto> allProdutos(){
        RealmResults<Produto> results = realm.where(Produto.class).findAll().sort("nome", Sort.ASCENDING);
        if(results.size()>0){
            return  results;
        }else{
            return null;
        }

    }

    public ArrayList<Produto> carregaProdutos() {
        ArrayList<Produto> produtos  = new ArrayList<Produto>();
        RealmResults<Produto> resultProduto= allProdutos();

        if (resultProduto != null && resultProduto.size() >0) {
            for (Produto produto : resultProduto) {
                 if(produto.getUnidade() !=null) {
                     produtos.add(produto);
                 }
            }
        }
        return produtos;

    }
}
