package com.br.minasfrango.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Funcionario;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.model.com.br.minasfrango.dto.ListaPedidoDTO;
import com.br.minasfrango.model.com.br.minasfrango.dto.PedidoDTO;
import com.br.minasfrango.service.ExportacaoService;
import com.br.minasfrango.service.ImportacaoService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExportData extends AsyncTask<Void, Void, Boolean> {
		
		private ProgressDialog dialog;
		private Context ctx;
		List<PedidoDTO> pedidoDTOS;
		
		public ExportData(Context ctx, List<PedidoDTO> pedidoDTOS) {
				dialog = new ProgressDialog(ctx);
				this.ctx = ctx;
				this.pedidoDTOS = pedidoDTOS;
		}
		
		@Override
		protected void onPreExecute() {
				super.onPreExecute();
				dialog.setMessage("Exportando dados...");
				dialog.show();
		}
		
		@Override
		protected void onPostExecute(Boolean aBoolean) {
				super.onPostExecute(aBoolean);
				if (dialog.isShowing()) {
						if (aBoolean) {
								dialog.dismiss();
						}
						Toast.makeText(ctx, "Arquivo Exportado!", Toast.LENGTH_LONG).show();
				}
		}
		
		@Override
		protected Boolean doInBackground(Void... voids) {
				try {
						
						//Aqui vem a chamada do service
						return exportaPedido(pedidoDTOS);
						
				} catch (Exception e) {
				
				}
				return false;
		}
		
		private boolean exportaPedido(List<PedidoDTO> pedidoDTOS) {
				ExportacaoService exportacaoService = new RetrofitConfig().getExportacaoService();
				ListaPedidoDTO listaPedidoDTO = new ListaPedidoDTO();
				listaPedidoDTO.setPedidosDTO(pedidoDTOS);
				Call<Boolean> callExportacao = exportacaoService.exportacaoPedido(listaPedidoDTO);
				try {
						Response<Boolean> response = callExportacao.execute();
						if (response.isSuccessful()) {
								return response.body();
						}
				} catch (IOException e) {
						e.printStackTrace();
				}
				return false;
		}
		
		
}
