package com.br.minasfrango.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.br.minasfrango.R;
import com.br.minasfrango.activity.adapter.ClienteAdapter;
import com.br.minasfrango.activity.adapter.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.dao.ClienteDAO;
import com.br.minasfrango.dao.RecebimentoDAO;
import com.br.minasfrango.dao.RotaDAO;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Rota;
import com.br.minasfrango.util.Mask;
import com.br.minasfrango.util.SessionManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClienteActivity extends BaseActivity implements RecyclerViewOnClickListenerHack, AdapterView.OnItemSelectedListener {
		Toolbar mToolbar;
		ClienteDAO clienteDAO = null;
		ArrayList<Cliente> clienteRota;
		private LinearLayout ln;
		public static final String PREFS_NAME = "Preferences";
		Bundle savedInstanceState;
		private RecyclerView clienteRecyclerView;
		ClienteAdapter adapter;
		ArrayAdapter adapterRota;
		ArrayList<Rota> rotas;
		private RotaDAO rotaDAO = null;
		Spinner rotaSpinner;
	
		RecebimentoDAO recebimentoDAO;
		
		@SuppressLint("MissingSuperCall")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				
				session = new SessionManager(getApplicationContext());
				if (session.checkLogin()) {
						finish();
				}
				
				setDrawer(savedInstanceState, R.layout.activity_main, "Clientes");
				initDrawer(savedInstanceState);
				clienteDAO = ClienteDAO.getInstace();
				recebimentoDAO = RecebimentoDAO.getInstace();
				
				initViews();
		}
		
		@Override
		protected void onStart() {
				super.onStart();
				LinearLayoutManager layoutManager = new LinearLayoutManager(ClienteActivity.this);
				layoutManager.setReverseLayout(true);
				layoutManager.setStackFromEnd(true);
				
				clienteRecyclerView.setLayoutManager(layoutManager);
				
				ArrayList<Cliente> clientes = clienteDAO.allClientes();
				if (clientes != null) {
						
						adapter = new ClienteAdapter(this, clientes);
				} else {
						clientes = new ArrayList<Cliente>();
						adapter = new ClienteAdapter(this, clientes);
				}
				
				rotaDAO = RotaDAO.getInstace();
				rotas = new ArrayList<Rota>();
				rotas = rotaDAO.carregaRota();
				adapterRota = new ArrayAdapter(this,
								android.R.layout.simple_list_item_1, rotas);
				
				rotaSpinner.setAdapter(adapterRota);
				rotaSpinner.setPrompt("Todas as Rotas");
				
				rotaSpinner.setOnItemSelectedListener(this);
				//Restaura as preferencias gravadas
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				clienteRota = new ArrayList<Cliente>();
				clienteRota.addAll(clientes);
				
				if (settings.getInt("Rotas", 0) == 0) {
						//seta spinner 0
						rotaSpinner.setSelection(0);
				} else {
						rotaSpinner.setSelection(settings.getInt("Rotas", 0));
						rotaSpinner.setOnItemSelectedListener(this);
				}
				
				adapter.setRecyclerViewOnClickListenerHack(this);
				clienteRecyclerView.setAdapter(adapter);
		}
		
		private void initViews() {
				clienteRecyclerView = (RecyclerView) findViewById(R.id.recycleview_cliente);
				rotaSpinner = (Spinner) findViewById(R.id.spn_rota);
				
				
		}
		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Rota rota = (Rota) adapterRota.getItem(position);
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("Rotas", position);
				//Confirma a gravação dos dados
				editor.commit();
				rotaSpinner.setSelection(position);
				
				carregaClientesPorRota(rota);
				;
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		
		}
		
		private void carregaClientesPorRota(Rota rota) {
				
				ArrayList<Cliente> aux = new ArrayList<Cliente>();
				for (Cliente cliente : clienteDAO.carregaClientesPorRota(rota)) {
						aux.add(cliente);
				}
				clienteRota.clear();
				clienteRota = aux;
				adapter.notifyDataSetChanged();
				adapter = new ClienteAdapter(this, clienteRota);
				
				adapter.setRecyclerViewOnClickListenerHack(this);
				clienteRecyclerView.setAdapter(adapter);
		}
		
		protected ClienteDAO getInstaceClienteDAO() {
				if (clienteDAO == null) {
						clienteDAO = ClienteDAO.getInstace();
				}
				return clienteDAO;
		}
		
		@Override
		public void onClickListener(View view, int position) {
				
				switch (view.getId()) {
						
						case R.id.btnVender:
								
								Intent intent = new Intent(this, VendasActivity.class);
								Cliente cliente = adapter.getItem(position);
								Bundle params = new Bundle();
								params.putSerializable("keyCliente", cliente);
								params.putString("dataVenda", new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
								intent.putExtras(params);
								startActivity(intent);
								
								break;
						
						case R.id.btnReceber:
								
								if (recebimentoDAO.recebimentosPorCliente(adapter.getItem(position)).size() > 0) {
										Intent receIntent = new Intent(this, RecebimentoActivity.class);
										Bundle receBundle = new Bundle();
										receBundle.putSerializable("keyCliente", adapter.getItem(position));
										receBundle.putString("dataVenda", new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
										receIntent.putExtras(receBundle);
										startActivity(receIntent);
								} else {
										Toast.makeText(ClienteActivity.this, "Cliente sem débito!", Toast.LENGTH_LONG).show();
								}
								break;
						case R.id.imgInfo:
								dialogDetalheCliente( adapter.getItem(position));
								break;
								
						case R.id.profileImageIv:
								dialogDetalheCliente(adapter.getItem(position));
								break;
						
				}
				
		}
		
		
		@Override
		public void onLongPressClickListener(View view, int position) {
		
		
		}
		
		private void dialogDetalheCliente(Cliente cliente) {
				
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClienteActivity.this);
				LayoutInflater inflater = this.getLayoutInflater();
				final View dialogView = inflater.inflate(R.layout.dialog_detalhe_cliente, null);
				dialogBuilder.setView(dialogView);
				
				TextView codigoClienteTextView;
				TextView razaoSocialTextView, qtdNotasAbertasTextView, valorMedioCompraTextView;
				TextView nomeFantasiaTextView, obsTextView, rgTextView;
				TextView enderecoTextView, pontoReferenciaTextView, cpfcnpjTextView;
				TextView bairroClienteTextView, telefoneTextView, contatoTextView;
				TextView cepClienteTextView, cidadeTextView, codigoLocalidadeTextView;
				
			//	dialogView.setContentView(R.layout.dialog_detalhe_cliente);
				
				codigoClienteTextView = (TextView) dialogView.findViewById(R.id.txt_codigo_cliente);
				razaoSocialTextView = (TextView) dialogView.findViewById(R.id.txt_razao_social);
				nomeFantasiaTextView = (TextView) dialogView.findViewById(R.id.txt_nome_fantasia);
				enderecoTextView = (TextView) dialogView.findViewById(R.id.txt_endereco);
				bairroClienteTextView = (TextView) dialogView.findViewById(R.id.txt_bairro);
				cepClienteTextView = (TextView) dialogView.findViewById(R.id.txt_cep);
				cidadeTextView = (TextView) dialogView.findViewById(R.id.txt_cidade);
				obsTextView = (TextView) dialogView.findViewById(R.id.txt_observacao);
				rgTextView = (TextView) dialogView.findViewById(R.id.txt_rg);
				valorMedioCompraTextView = (TextView) dialogView.findViewById(R.id.txt_vlr_medio_compra);
				
				codigoLocalidadeTextView = (TextView) dialogView.findViewById(R.id.txt_codigo_localidade);
				telefoneTextView = (TextView) dialogView.findViewById(R.id.txt_telefone);
				contatoTextView = (TextView) dialogView.findViewById(R.id.txt_contato);
				pontoReferenciaTextView = (TextView) dialogView.findViewById(R.id.txt_ponto_REF);
				cpfcnpjTextView = (TextView) dialogView.findViewById(R.id.txt_cpf_cnpj);
				qtdNotasAbertasTextView = (TextView) dialogView.findViewById(R.id.txt_qtd_notas_abertas);
				// Custom Android Allert Dialog Title
				dialogBuilder.setTitle("Informações do Cliente");
				nomeFantasiaTextView.setText(cliente.getNome());
				razaoSocialTextView.setText(cliente.getRazaoSocial());
				codigoClienteTextView.setText(String.valueOf(cliente.getId()));
				enderecoTextView.setText(cliente.getEndereco());
				bairroClienteTextView.setText(cliente.getBairro());
				cepClienteTextView.setText(String.valueOf(cliente.getCep()));
				cidadeTextView.setText(cliente.getCidade());
				telefoneTextView.setText(cliente.getTelefone());
				AlertDialog b = dialogBuilder.create();
				b.show();
				
		}
		
		
}
