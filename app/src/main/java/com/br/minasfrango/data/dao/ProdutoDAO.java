package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.Produto;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;

/**
 * Created by 04717299302 on 26/12/2016.
 */

public class ProdutoDAO extends DAO<Produto> {


    private RealmQuery<Produto> where() {
        return realm.where(Produto.class);
    }
    public static  ProdutoDAO getInstace(){
        return new ProdutoDAO();
    }
    public ProdutoDAO()
    {
        super();
    }

    public RealmResults<Produto> allProdutos(){
        RealmResults<Produto> results = where().findAll().sort("nome", Sort.ASCENDING);
        if(results.size()>0){
            return  results;
        }else{
            return null;
        }

    }

    public ArrayList<Produto> getAll() {
        ArrayList<Produto> produtos  = new ArrayList<Produto>();
        RealmResults<Produto> resultProduto= allProdutos();

        if (resultProduto != null && resultProduto.size() >0) {
            for (Produto produto : resultProduto) {
                 if(produto.getUnidade() !=null) {
                     Produto aux= new Produto();
                     aux.setNome(produto.getNome());
                     aux.setId(produto.getId());
                     aux.setUnidade(produto.getUnidade());
                     aux.setQuantidade(produto.getQuantidade());
                     produtos.add(aux);
                 }
            }
        }
        return produtos;

    }
}
