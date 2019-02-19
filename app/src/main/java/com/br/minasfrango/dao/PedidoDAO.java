package com.br.minasfrango.dao;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Pedido;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.internal.IOException;

public class PedidoDAO {
		
		Realm realm ;
		
		public static  PedidoDAO getInstace(){
				return new PedidoDAO();
		}
		public PedidoDAO() {
				realm=Realm.getDefaultInstance();
		}
		
		public  long addPedido(Pedido pedido){
				long id;
				if(realm.where(Pedido.class).max("id")!=null){
						id =  (long) (realm.where(Pedido.class).max("id").intValue() +1);
				}else{
						id=1;
				}
				try{
						
						pedido.setId(id);
						
						realm.beginTransaction();
						realm.copyToRealmOrUpdate(pedido);
						realm.commitTransaction();
						
				}catch (RealmException e){
						
						realm.cancelTransaction();
						
				}
		return id;
		}
		
		public  Pedido searchPedido(Cliente cliente,String data){
				Pedido pedido = new Pedido();
				Date dataFiltro = null;
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				
				
				try {
						dataFiltro = (Date) format.parse(data);
				} catch (ParseException e) {
						e.printStackTrace();
				}
				
				
				RealmQuery<Pedido> pedidoRealmQuery = realm.where(Pedido.class);
				pedidoRealmQuery.equalTo("codigoCliente",cliente.getId()).equalTo("dataPedido",dataFiltro);
				RealmResults<Pedido> results=pedidoRealmQuery.findAll();
				ArrayList<ItemPedido> itens= new ArrayList<ItemPedido>();
				
				if(results.size()>0 && results!=null){
						
						results.first();
						pedido.setCodigoFuncionario(results.get(0).getCodigoFuncionario());
						pedido.setCodigoCliente(results.get(0).getCodigoCliente());
						pedido.setId(results.get(0).getId());
						pedido.setValorTotal(results.get(0).getValorTotal());
						pedido.setTipoRecebimento(results.get(0).getTipoRecebimento());
						pedido.setDataPedido(results.get(0).getDataPedido());
						pedido.setItens((RealmList<ItemPedido>) results.get(0).getItens());
						
				}
				
				return  pedido;
				
		}
		
		
		
		public ArrayList<Cliente> positivados(String data){
				RealmQuery<Pedido> queryProduto = realm.where(Pedido.class);
				Date dataFiltro = null;
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				ArrayList<Cliente> clientes = new ArrayList<Cliente>();
				
				try {
						dataFiltro = (Date) format.parse(data);
				} catch (ParseException e) {
						e.printStackTrace();
				}
				
				RealmResults<Pedido> results=queryProduto.equalTo("dataPedido",dataFiltro).findAll();
				if (results.size()>0 ){
						for (int i= 0;i<results.size();i++){
								Pedido pedido = new Pedido();
								Cliente cliente = new Cliente();
								pedido.setCodigoCliente(results.get(i).getCodigoCliente());
								pedido.setCodigoFuncionario(results.get(i).getCodigoFuncionario());
								cliente.setId(pedido.getCodigoCliente());
								clientes.add(cliente);
								
						}
				}
				return clientes;
		}
		
		public ArrayList<Cliente> naoPositivados(String data) {
				
				RealmQuery<Pedido> queryProduto = realm.where(Pedido.class);
				Date dataFiltro = null;
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				ArrayList<Cliente> clientes = new ArrayList<Cliente>();
				
				try {
						dataFiltro = (Date) format.parse(data);
				} catch (ParseException e) {
						e.printStackTrace();
				}
				
				RealmResults<Pedido> results = queryProduto.notEqualTo("dataPedido", dataFiltro).findAll();
				if (results.size() > 0) {
						for (int i = 0; i < results.size(); i++) {
								Pedido pedido = new Pedido();
								Cliente cliente = new Cliente();
								pedido.setCodigoCliente(results.get(i).getCodigoCliente());
								pedido.setCodigoFuncionario(results.get(i).getCodigoFuncionario());
								cliente.setId(pedido.getCodigoCliente());
								clientes.add(cliente);
								
						}
				}
				return clientes;
		}
		
		
		public boolean isPositivado(Cliente cliente,String data){
				RealmQuery<Pedido> queryProduto = realm.where(Pedido.class);
				Date dataFiltro = null;
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				ArrayList<Cliente> clientes = new ArrayList<Cliente>();
				
				try {
						dataFiltro = (Date) format.parse(data);
				} catch (ParseException e) {
						e.printStackTrace();
				}
				
				RealmResults<Pedido> results=queryProduto.equalTo("dataPedido",dataFiltro).equalTo("codigoCliente",cliente.getId()).findAll();
				
				if (results.size()>0  ){
						return  true;
				}
				return false;
		}
		
		public void updatePedido(Pedido pedido) {
				try {
						
						realm.beginTransaction();
						realm.copyToRealmOrUpdate(pedido);
						realm.commitTransaction();
						
				}catch (RealmException ex){
						realm.cancelTransaction();
				}
				
		}
		
		public void remove(Pedido pedidoRealizado) {
				
				RealmResults<Pedido> result = realm.where(Pedido.class).equalTo("id",pedidoRealizado.getId()).findAll();
				
				
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
}
