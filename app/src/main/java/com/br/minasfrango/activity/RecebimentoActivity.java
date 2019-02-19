package com.br.minasfrango.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.br.minasfrango.R;
import com.br.minasfrango.activity.adapter.ItemPedidoAdapter;
import com.br.minasfrango.activity.adapter.RecebimentoAdapter;
import com.br.minasfrango.activity.adapter.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.dao.ItemPedidoDAO;
import com.br.minasfrango.dao.RecebimentoDAO;
import com.br.minasfrango.dao.TipoRecebimentoDAO;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.Recebimento;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RecebimentoActivity extends AppCompatActivity  {
		
		private Cliente cliente;
		Toolbar toolbar;
		private RecyclerView recebimentosRecyclerView;
		RecebimentoDAO recebimentoDAO;
		TipoRecebimentoDAO tipoRecebimentoDAO;
		List<Recebimento> recebimentos;
		private ArrayAdapter<String> adapterTipoRecebimento;
		Button btnPrint;
		
		Spinner spinnerFormaPagamento;
		EditText valorAmortizacao;
		
		RecebimentoAdapter adapter;
		int idTipoRecebimento;
		Button confirmaAmortizacao;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_recebimento);
				toolbar = (Toolbar) findViewById(R.id.toolbar);
				setSupportActionBar(toolbar);
				
				cliente = getParams();
				initView();
		}
		
		private void initView() {
				
				TextView codigoClienteTextView;
				
				TextView nomeFantasiaTextView;
				TextView enderecoTextView;
				
				codigoClienteTextView = (TextView) findViewById(R.id.txt_codigo_cliente);
				nomeFantasiaTextView = (TextView) findViewById(R.id.txt_nome_fantasia);
				enderecoTextView = (TextView) findViewById(R.id.txt_endereco);
				
				valorAmortizacao = (EditText) findViewById(R.id.edt_amortizacao);
				spinnerFormaPagamento = (Spinner) findViewById(R.id.spinner_tipo_recebimento);
				confirmaAmortizacao = (Button) findViewById(R.id.btn_confirmar_amortizacao);
				
				
				
				codigoClienteTextView.setText(String.valueOf(cliente.getId()));
				enderecoTextView.setText(cliente.getEndereco());
				nomeFantasiaTextView.setText(cliente.getRazaoSocial());
				
				btnPrint = findViewById(R.id.btn_imprimir_recibo);
				
				toolbar = (Toolbar) findViewById(R.id.toolbar);
				toolbar.setTitle("Recebimentos");
				setSupportActionBar(toolbar);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				
				recebimentosRecyclerView = (RecyclerView) findViewById(R.id.recycleview_recebimentos);
				
				
		}
		
		private Cliente getParams() {
				Bundle args = getIntent().getExtras();
				Cliente cliente = (Cliente) args.getSerializable("keyCliente");
				return cliente;
		}
		
		@Override
		protected void onStart() {
				super.onStart();
				recebimentoDAO = RecebimentoDAO.getInstace();
				recebimentos = recebimentoDAO.recebimentosPorCliente(cliente);
				
				LinearLayoutManager layoutManager = new LinearLayoutManager(RecebimentoActivity.this);
				layoutManager.setReverseLayout(true);
				layoutManager.setStackFromEnd(true);
				recebimentosRecyclerView.setLayoutManager(layoutManager);
				adapter = new RecebimentoAdapter(RecebimentoActivity.this, recebimentos);
				recebimentosRecyclerView.setAdapter(adapter);
				btnPrint.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
								finish();
						}
				});
				
				tipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();
				ArrayList<String> tiposRecebimentos = tipoRecebimentoDAO.carregaFormaPagamentoAmortizacao();
				
				adapterTipoRecebimento = new ArrayAdapter<String>(RecebimentoActivity.this, android.R.layout.simple_spinner_item, tiposRecebimentos);
				spinnerFormaPagamento.setAdapter(adapterTipoRecebimento);
				spinnerFormaPagamento.setSelection(0);
				
				
				valorAmortizacao.setText(NumberFormat.getCurrencyInstance().format(0).replace("R$", ""));
				spinnerFormaPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								
								idTipoRecebimento = tipoRecebimentoDAO.codigoFormaPagamento((String) parent.getAdapter().getItem(position));
								
								
						}
						
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						
						}
				});
				
				confirmaAmortizacao.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
								calculaAmortizacao(valorAmortizacao.getText().toString(),recebimentos);
						}
				});
				
		}

		
		private void calculaAmortizacao( String valorAmortizacao,List<Recebimento> recebimentos) {
				
				
				int i=0;
				double saldo=0.0;
				double valorAmortizado = 0;
				
				
				try {
						//Converte o edit text do preco unitario e pega sempre o valor digitado
						Number numberValorAmortizado = NumberFormat.getCurrencyInstance().parse("R$" + valorAmortizacao);
					  valorAmortizado=numberValorAmortizado.doubleValue();
						while (saldo>=0){
							saldo	=valorAmortizado-recebimentos.get(i).getValorVenda();
							if(saldo>=0){
									recebimentos.get(i).setValorAmortizado(recebimentos.get(i).getValorVenda());
									recebimentos.get(i).setTipoRecebimento(idTipoRecebimento);
							}else{
									recebimentos.get(i).setValorAmortizado(valorAmortizado);
									recebimentos.get(i).setTipoRecebimento(idTipoRecebimento);
							}
								recebimentoDAO.updateRecebimento(recebimentos.get(i));
								
								adapter.updateListItem(recebimentos.get(i), i);
								valorAmortizado=saldo;
							i++;
						}
						
						
						
				} catch (ParseException e) {
						e.printStackTrace();
				}
				
		}
}
