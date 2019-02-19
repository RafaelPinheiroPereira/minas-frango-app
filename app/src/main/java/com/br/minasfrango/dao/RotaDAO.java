package com.br.minasfrango.dao;

import com.br.minasfrango.model.Rota;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RotaDAO {
		Realm realm;
		RealmResults<Rota> result;
		
		public static RotaDAO getInstace() {
				return new RotaDAO();
		}
		
		public RotaDAO() {
				realm = Realm.getDefaultInstance();
		}
		
		public ArrayList<Rota> carregaRota() {
				ArrayList<Rota> rotas = new ArrayList<Rota>();
				result = realm.where(Rota.class).findAll();
				
				
				
				if (result != null) {
						
						for (int i=0;i< result.size();i++) {
								Rota rota = new Rota(result.get(i).getId(),result.get(i).getFuncionario(),result.get(i).getNome());
								rotas.add(rota);
						}
				}
				return rotas;
				
		}
}
