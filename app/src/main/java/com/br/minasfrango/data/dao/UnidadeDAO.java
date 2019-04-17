package com.br.minasfrango.data.dao;

import com.br.minasfrango.data.pojo.Produto;
import com.br.minasfrango.data.pojo.Unidade;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.ArrayList;

/**
 * Created by 04717299302 on 28/12/2016.
 */

public class UnidadeDAO {
		Realm realm;
		RealmResults<Unidade> result;
		
		public static UnidadeDAO getInstace() {
				return new UnidadeDAO();
		}
		
		public UnidadeDAO() {
				realm = Realm.getDefaultInstance();
		}
		
		public ArrayList<String> carregaUnidadesProduto(Produto produto) {
				ArrayList<String> strUnidades = new ArrayList<String>();
				RealmQuery<Unidade> query = realm.where(Unidade.class);
				
				query.equalTo("chavesUnidade.idProduto", produto.getId());
				result = query.findAll();
				
				if (result != null) {
						for (int i = 0; i < result.size(); i++) {
								Unidade unidade = new Unidade(result.get(i).getId(),
												result.get(i).getChavesUnidade(),
												result.get(i).getNome(),
												result.get(i).getUnidadePadrao());
								
								if (!unidade.getUnidadePadrao().equals("S")) {
										strUnidades.add(unidade.getId().split("-")[0]);
								}
						}
				}
				return strUnidades;
				
		}
		
		public String carregaUnidadePadraoProduto(Produto produto) {
				String[] unidadePadrao =null;
				RealmQuery<Unidade> query = realm.where(Unidade.class);
				query.equalTo("chavesUnidade.idProduto", produto.getId());
				result = query.findAll();
				
				if (result != null) {
						for (int i = 0; i < result.size(); i++) {
								Unidade unidade = new Unidade();
								unidade.setId(result.get(i).getId());
								unidade.setUnidadePadrao(result.get(i).getUnidadePadrao());
								if(unidade.getUnidadePadrao().equals("S")){
										unidadePadrao= unidade.getId().split("-");
										break;
								}
								
								
								
						}
						
				}
				return unidadePadrao[0];
				
				
		}
}
