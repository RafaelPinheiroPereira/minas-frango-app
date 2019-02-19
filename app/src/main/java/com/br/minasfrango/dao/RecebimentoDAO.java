package com.br.minasfrango.dao;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Recebimento;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;

public class RecebimentoDAO {
		
		Realm realm;
		Recebimento recebimento;
		
		public static RecebimentoDAO getInstace() {
				return new RecebimentoDAO();
		}
		
		public RecebimentoDAO() {
				realm = Realm.getDefaultInstance();
		}
		
		
		
		
		public ArrayList<Recebimento> recebimentosPorCliente(Cliente cliente) {
				ArrayList<Recebimento> recebimentos = new ArrayList<Recebimento>();
				
				RealmResults<Recebimento> results = realm.where(Recebimento.class).equalTo("idCliente",cliente.getId()).sort("dataVenda",Sort.ASCENDING).findAll();
				
				if (results != null && results.size() > 0) {
						
						for (int i = 0; i < results.size(); i++) {
								
								 Recebimento recebimento= new Recebimento();
								 recebimento.setId(results.get(i).getId());
								 recebimento.setIdCliente(results.get(i).getIdCliente());
								 recebimento.setIdFuncionario(results.get(i).getIdFuncionario());
								 recebimento.setValorVenda(results.get(i).getValorVenda());
								 recebimento.setIdVenda(results.get(i).getIdVenda());
								 recebimento.setDataVenda(results.get(i).getDataVenda());
								 recebimento.setValorAmortizado(results.get(i).getValorAmortizado());
						     recebimentos.add(recebimento);
						}
						
				}
				
				return recebimentos;
				
		}
		
		public void updateRecebimento(Recebimento recebimento) {
				realm.beginTransaction();
				realm.copyToRealmOrUpdate(recebimento);
				realm.commitTransaction();
		}
		
		public  void addRecibo(Recebimento recebimento){
				try{
				long id;
				if(realm.where(Recebimento.class).max("id")!=null){
						id =  (long) (realm.where(Recebimento.class).max("id").intValue() +1);
				}else{
						id=1;
				}
				
						
						recebimento.setId(id);
						
						realm.beginTransaction();
						realm.copyToRealmOrUpdate(recebimento);
						realm.commitTransaction();
						
				}catch (RealmException e){
						
						realm.cancelTransaction();
						
				}
				
		}
		
}
