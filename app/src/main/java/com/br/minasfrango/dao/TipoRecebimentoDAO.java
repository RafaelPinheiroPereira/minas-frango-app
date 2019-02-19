package com.br.minasfrango.dao;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.TipoRecebimento;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by 04717299302 on 16/01/2017.
 */

public class TipoRecebimentoDAO {

    RealmResults<TipoRecebimento> result;
    Realm realm ;

    public static  TipoRecebimentoDAO getInstace(){
        return new TipoRecebimentoDAO();
    }
    public TipoRecebimentoDAO() {
        realm=Realm.getDefaultInstance();
    }

    public ArrayList<String> carregaFormaPagamentoCliente(Cliente cliente) {
        ArrayList<String> formas  = new ArrayList<String>();
        formas.add("FORMAS DE PAGAMENTO");
        RealmQuery<TipoRecebimento> formaPagamentoRealmQuery= realm.where(TipoRecebimento.class);
        result = formaPagamentoRealmQuery.findAll();

        if (result != null) {
            
            for (TipoRecebimento tipoRecebimento : result) {
                formas.add(String.valueOf(tipoRecebimento.getNome()));
            }
        }
        return formas;

    }


    
    

    public int codigoFormaPagamento(String descricaoFormaPagamento) {

        RealmQuery<TipoRecebimento> formaPagamentoRealmQuery= realm.where(TipoRecebimento.class);
        result = formaPagamentoRealmQuery.equalTo("nome",descricaoFormaPagamento).findAll();
        int codigo=0;
        if (result != null) {

            for (TipoRecebimento tipoRecebimento : result) {
                 codigo= (int) tipoRecebimento.getId();
            }
        }
        return codigo;

    }
		
		public ArrayList<String>  carregaFormaPagamentoAmortizacao() {
        ArrayList<String> formas  = new ArrayList<String>();
        RealmQuery<TipoRecebimento> formaPagamentoRealmQuery= realm.where(TipoRecebimento.class);
        result = formaPagamentoRealmQuery.equalTo("id",1).findAll();
        
        if (result != null) {
            
            for (TipoRecebimento tipoRecebimento : result) {
                formas.add(String.valueOf(tipoRecebimento.getNome()));
            }
        }
        return formas;
		}
}
