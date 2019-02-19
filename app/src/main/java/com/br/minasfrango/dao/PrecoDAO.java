package com.br.minasfrango.dao;

import com.br.minasfrango.model.Preco;
import com.br.minasfrango.model.Produto;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


/**
 * Created by 04717299302 on 28/12/2016.
 */

public class PrecoDAO {
    Realm realm ;

    public static  PrecoDAO getInstace(){
        return new PrecoDAO();
    }
    public PrecoDAO() {
        realm=Realm.getDefaultInstance();
    }


    public Preco carregaPrecoProduto(Produto produto) {
        Preco preco  = new Preco();
        preco.setValor(0.0);
        RealmQuery<Preco> query = realm.where(Preco.class);
        query.equalTo("chavesPreco.idProduto",produto.getId()).equalTo("chavesPreco.unidadeProduto",produto.getUnidade());
        RealmResults<Preco> result =  query.findAll();

        if (result != null&& result.size()>0) {
            for (Preco aux: result) {
                  if(aux.getValor()!=0) {
                      return aux;
                  }

            }
        }

      return preco;
    }
    public Preco carregaPrecoUnidadeProduto(Produto produto,String unidade) {
        Preco preco  = new Preco();
        preco.setValor(0.0);
        RealmQuery<Preco> query = realm.where(Preco.class);
        
        query.equalTo("chavesPreco.idProduto",produto.getId()).equalTo("chavesPreco.unidadeProduto",unidade);
        RealmResults<Preco> result =  query.findAll();

        if (result != null&& result.size()>0) {
            for (Preco aux: result) {
                if(aux.getValor()!=0) {
                    return aux;
                }

            }
        }

        return preco;
    }
}
