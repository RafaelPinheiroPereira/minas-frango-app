package com.br.minasfrango.dao;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Funcionario;
import com.br.minasfrango.model.Localidade;
import com.br.minasfrango.model.Rota;

import java.util.ArrayList;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 04717299302 on 16/12/2016.
 */

public class ClienteDAO {
		Realm realm;
		Cliente cliente;
		
		public static ClienteDAO getInstace() {
				return new ClienteDAO();
		}
		
		public ClienteDAO() {
				realm = Realm.getDefaultInstance();
		}
		
		public ArrayList<Cliente> allClientes() {
				ArrayList<Cliente> clientes = new ArrayList<Cliente>();
				
				RealmResults<Cliente> results = realm.where(Cliente.class).sort("nome",Sort.DESCENDING).findAll();
				
				if (results != null && results.size() > 0) {
						
						for (int i = 0; i < results.size(); i++) {
								Funcionario funcionario = new Funcionario(results.get(i).getLocalidade().getRota().getFuncionario().getId()
												, results.get(i).getLocalidade().getRota().getFuncionario().getSenha(),
												results.get(i).getLocalidade().getRota().getFuncionario().getNome(), results.get(i).getLocalidade().getRota().getFuncionario().getTipoFuncionario());
								Rota rota = new Rota(results.get(i).getLocalidade().getRota().getId(), funcionario,
												results.get(i).getLocalidade().getRota().getNome());
								Localidade localidade = new Localidade(results.get(i).getLocalidade().getId(), results.get(i).getLocalidade().getNome(),rota );
								Cliente aux = new Cliente(results.get(i).getId(),
												results.get(i).getNome(),
												results.get(i).getRazaoSocial(),
												localidade,
												results.get(i).getEndereco(),
												results.get(i).getNumero(),
												results.get(i).getBairro(),
												results.get(i).getCidade(),
												results.get(i).getCep(),
												results.get(i).getReferencia(),
												results.get(i).getTelefone(),
												results.get(i).getCpf());
								clientes.add(aux);
						}
						
				}
				
				return clientes;
				
		}
		
		public ArrayList<Cliente> carregaClientesPorRota(Rota rotaToSearch) {
				ArrayList<Cliente> clientes = new ArrayList<Cliente>();
				RealmQuery<Cliente> clienteRealmQuery = realm.where(Cliente.class);
				RealmResults<Cliente> results = clienteRealmQuery.equalTo("localidade.rota.id", rotaToSearch.getId()).sort("nome",Sort.DESCENDING).findAll();
				if (results != null) {
						for (int i = 0; i < results.size(); i++) {
								Funcionario funcionario = new Funcionario(results.get(i).getLocalidade().getRota().getFuncionario().getId()
												, results.get(i).getLocalidade().getRota().getFuncionario().getSenha(),
												results.get(i).getLocalidade().getRota().getFuncionario().getNome(), results.get(i).getLocalidade().getRota().getFuncionario().getTipoFuncionario());
								Rota rota = new Rota(results.get(i).getLocalidade().getRota().getId(), funcionario,
												results.get(i).getLocalidade().getRota().getNome());
								Localidade localidade = new Localidade(results.get(i).getLocalidade().getId(), results.get(i).getLocalidade().getNome(),rota );
								Cliente aux = new Cliente(results.get(i).getId(),
												results.get(i).getNome(),
												results.get(i).getRazaoSocial(),
												localidade,
												results.get(i).getEndereco(),
												results.get(i).getNumero(),
												results.get(i).getBairro(),
												results.get(i).getCidade(),
												results.get(i).getCep(),
												results.get(i).getReferencia(),
												results.get(i).getTelefone(),
												results.get(i).getCpf());
								clientes.add(aux);
						}
				}
				return clientes;
				
		}
}
