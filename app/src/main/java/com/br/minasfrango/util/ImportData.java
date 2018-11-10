package com.br.minasfrango.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Funcionario;
import com.br.minasfrango.model.ImportacaoDados;
import com.br.minasfrango.model.Preco;
import com.br.minasfrango.model.Produto;
import com.br.minasfrango.model.Rota;
import com.br.minasfrango.model.TipoRecebimento;
import com.br.minasfrango.model.Unidade;
import com.br.minasfrango.service.ImportacaoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pc on 13/01/2016.
 */
public class ImportData extends AsyncTask<Void, Void, Boolean> {
		
		private ProgressDialog dialog;
		private Context ctx;
		ArrayList<ImportacaoDados> importacaoDados;
		Funcionario funcionario;
		
		public ImportData(Context ctx, ArrayList<ImportacaoDados> importacaoDados, Funcionario funcionario) {
				dialog = new ProgressDialog(ctx);
				this.importacaoDados = importacaoDados;
				this.ctx = ctx;
				this.funcionario = funcionario;
		}
		
		@Override
		protected void onPreExecute() {
				super.onPreExecute();
				dialog.setMessage("Importando dados...");
				dialog.show();
		}
		
		@Override
		protected void onPostExecute(Boolean aBoolean) {
				super.onPostExecute(aBoolean);
				if (dialog.isShowing()) {
						dialog.dismiss();
						Toast.makeText(ctx, "Importacao Realizada", Toast.LENGTH_LONG).show();
				}
		}
		
		@Override
		protected Boolean doInBackground(Void... voids) {
				try {
						
						realizaImportacao(importacaoDados);
						
				} catch (Exception e) {
				
				}
				return true;
		}
		
		public boolean realizaImportacao(ArrayList<ImportacaoDados> importacaoDados) throws IOException {
				
				for (ImportacaoDados aux : importacaoDados) {
						
						if (aux.isSelected()) {
								switch (aux.getCodigo()) {
										case "1":
												
												importarClientes();
												
												break;
										case "2":
												importarTipoRecebimentos();
												break;
										case "3":
												
												importarUnidades();
												
												break;
										case "4":
												
												importarProdutos();
												
												break;
										case "5":
												importarPrecos();
												break;
								}
						}
				}
				return true;
				
		}
		
		private void importarClientes() {
				ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
				
				Funcionario aux = new Funcionario();
				aux.setId(1);
				Call<List<Cliente>> clientes = importacaoService.importacaoCliente(aux);
				
				clientes.enqueue(new Callback<List<Cliente>>() {
						@Override
						public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
								List<Cliente> clientes = response.body();
								
								//salva no realm

// Get a Realm instance for this thread
								Realm realm = Realm.getDefaultInstance();
								
								realm.beginTransaction();
								for (Cliente aux : clientes) {
										realm.copyToRealmOrUpdate(aux);
								}
								realm.commitTransaction();
								
								RealmResults<Cliente> results = realm.where(Cliente.class).findAll();
								//perguntar para milson se eh para excluir tudo
								if (results.size() >= 0) {
										
										for (int i = 0; i < results.size(); i++) {
												Log.d("Clientes", results.get(i).getRazaoSocial());
												
										}
								}
								
								
						}
						
						@Override
						public void onFailure(Call<List<Cliente>> call, Throwable t) {
								dialog.dismiss();
								Log.e("ImportacaoService   ", "Erro ao importar:" + t.getMessage());
								
						}
				});
		}
		
		private void importarProdutos() {
				ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
				
				Produto aux = new Produto();
				
				final Call<List<Produto>> produtos = importacaoService.importacaoProduto();
				
				produtos.enqueue(new Callback<List<Produto>>() {
						@Override
						public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
								List<Produto> produtos = response.body();
								
								Realm realm = Realm.getDefaultInstance();
								
								realm.beginTransaction();
								for (Produto aux : produtos) {
										realm.copyToRealmOrUpdate(aux);
								}
								realm.commitTransaction();
								
								RealmResults<Produto> results = realm.where(Produto.class).findAll();
								//perguntar para milson se eh para excluir tudo
								if (results.size() >= 0) {
										
										for (int i = 0; i < results.size(); i++) {
												Log.d("Produtos", results.get(i).getNome());
												
										}
								}
								
								
						}
						
						@Override
						public void onFailure(Call<List<Produto>> call, Throwable t) {
								dialog.dismiss();
								Log.e("ImportacaoService   ", "Erro ao importar:" + t.getMessage());
								
						}
				});
		}
		
		private void importarTipoRecebimentos() {
				ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
				
				final Call<List<TipoRecebimento>> rotas = importacaoService.importacaoTipoRecebimento();
				
				rotas.enqueue(new Callback<List<TipoRecebimento>>() {
						@Override
						public void onResponse(Call<List<TipoRecebimento>> call, Response<List<TipoRecebimento>> response) {
								List<TipoRecebimento> tipos = response.body();
								
								Realm realm = Realm.getDefaultInstance();
								
								realm.beginTransaction();
								for (TipoRecebimento aux : tipos) {
										realm.copyToRealmOrUpdate(aux);
								}
								realm.commitTransaction();
								
								RealmResults<TipoRecebimento> results = realm.where(TipoRecebimento.class).findAll();
								//perguntar para milson se eh para excluir tudo
								if (results.size() >= 0) {
										
										for (int i = 0; i < results.size(); i++) {
												Log.d("TipoRecebimento", results.get(i).getNome());
												
										}
								}
								
								
						}
						
						@Override
						public void onFailure(Call<List<TipoRecebimento>> call, Throwable t) {
								dialog.dismiss();
								Log.e("ImportacaoService   ", "Erro ao importar:" + t.getMessage());
								
						}
				});
		}
		
		private void importarUnidades() {
				ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
				
				final Call<List<Unidade>> unidades = importacaoService.importacaoUnidade();
				
				unidades.enqueue(new Callback<List<Unidade>>() {
						@Override
						public void onResponse(Call<List<Unidade>> call, Response<List<Unidade>> response) {
								List<Unidade> unidades = response.body();
								
								Realm realm = Realm.getDefaultInstance();
								
								realm.beginTransaction();
								for (Unidade aux : unidades) {
										aux.setId(aux.getChavesUnidade().getIdUnidade() + aux.getChavesUnidade().getIdProduto());
										realm.copyToRealmOrUpdate(aux);
								}
								realm.commitTransaction();
								
								RealmResults<Unidade> results = realm.where(Unidade.class).findAll();
								//perguntar para milson se eh para excluir tudo
								if (results.size() >= 0) {
										
										for (int i = 0; i < results.size(); i++) {
												Log.d("Unidades", results.get(i).getNome());
												
										}
								}
								
								
						}
						
						@Override
						public void onFailure(Call<List<Unidade>> call, Throwable t) {
								dialog.dismiss();
								Log.e("ImportacaoService   ", "Erro ao importar:" + t.getMessage());
								
						}
				});
		}
		
		private void importarPrecos() {
				ImportacaoService importacaoService = new RetrofitConfig().getImportacaoService();
				
				final Call<List<Preco>> precos = importacaoService.importacaoPreco();
				
				precos.enqueue(new Callback<List<Preco>>() {
						@Override
						public void onResponse(Call<List<Preco>> call, Response<List<Preco>> response) {
								List<Preco> precos = response.body();
								
								Realm realm = Realm.getDefaultInstance();
								
								realm.beginTransaction();
								for (Preco aux : precos) {
										aux.setId(aux.getChavesPreco().getId() + "-" + aux.getChavesPreco().getIdCliente() + "-" +
														aux.getChavesPreco().getIdProduto() + "-" + aux.getChavesPreco().getUnidadeProduto());
										realm.copyToRealmOrUpdate(aux);
								}
								realm.commitTransaction();
								
								RealmResults<Preco> results = realm.where(Preco.class).findAll();
								//perguntar para milson se eh para excluir tudo
								if (results.size() >= 0) {
										
										for (int i = 0; i < results.size(); i++) {
												Log.d("Preco", results.get(i).getId() + " - " + results.get(i).getValor());
												
										}
								}
								
						}
						
						@Override
						public void onFailure(Call<List<Preco>> call, Throwable t) {
								dialog.dismiss();
								Log.e("ImportacaoService   ", "Erro ao importar:" + t.getMessage());
								
						}
				});
		}
		
}