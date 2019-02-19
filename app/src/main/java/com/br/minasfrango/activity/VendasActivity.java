package com.br.minasfrango.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.br.minasfrango.R;
import com.br.minasfrango.activity.adapter.ItemPedidoAdapter;
import com.br.minasfrango.activity.adapter.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.dao.ItemPedidoDAO;
import com.br.minasfrango.dao.PedidoDAO;
import com.br.minasfrango.dao.PrecoDAO;
import com.br.minasfrango.dao.ProdutoDAO;
import com.br.minasfrango.dao.UnidadeDAO;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.ItemPedidoID;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.model.Preco;
import com.br.minasfrango.model.Produto;
import com.br.minasfrango.model.TipoRecebimento;
import com.br.minasfrango.dao.TipoRecebimentoDAO;
import com.br.minasfrango.util.SessionManager;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.exceptions.RealmException;

public class VendasActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {
		
		Toolbar mToolbar;
		Cliente cliente;
		private ProdutoDAO produtoDAO = null;
		ArrayList<Produto> produtos;
		RecyclerView itemPedidoRecyclerView;
		EditText qtdEditText,qtdBicos;
		EditText precoEditText;
		Spinner uniSpinner;
		Button addButton;
		Spinner formaPagamentoSpinner;
		private PrecoDAO precoDAO;
		private UnidadeDAO unidadeDAO;
		TipoRecebimentoDAO tipoRecebimentoDAO;
		ArrayAdapter<String> adapter;
		ArrayAdapter<String> adapterFormaPagamento;
		Preco preco;
		ArrayList<ItemPedido> itens;
		ItemPedidoAdapter adapterItemPedidoAdapter;
		
		ArrayList<String> codigosProdutos;
		AutoCompleteTextView codigoProdutoAutoCompleteTextView;
		ArrayAdapter<String> adaptadorCodigoProduto;
		ArrayAdapter<String> adaptadorDescricaoProduto;
		ArrayList<String> descricoesProdutos;
		Spinner spnProduto;
		Button confirmarPedidoButton, cancelarPedidoButton;
		Produto produtoSelecionado;
		LinearLayoutManager layoutManager;
		PedidoDAO pedidoDAO;
		ArrayAdapter<String> adapterUnidade;
		ArrayList<String> tiposRecebimento;
		String formaPagamento;
		TextView valorTotalProduto, valorTotalTextView;
		String dataVenda;
		Pedido pedidoRealizado;
		double quantidadeAtual = 0;
		double valorTotal = 0;
		private String codigoUnidade;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_vendas);
				cliente = getParams();
				initView();
				
				produtoDAO = ProdutoDAO.getInstace();
				pedidoDAO = PedidoDAO.getInstace();
				
				carregaPositivados();
				
				//Formas de Pagamento
				tipoRecebimentoDAO = tipoRecebimentoDAO.getInstace();
				tiposRecebimento = tipoRecebimentoDAO.carregaFormaPagamentoCliente(cliente);
				
				adapterFormaPagamento = new ArrayAdapter<String>(VendasActivity.this, android.R.layout.simple_list_item_1, tiposRecebimento);
				formaPagamentoSpinner.setAdapter(adapterFormaPagamento);
				
				layoutManager = new LinearLayoutManager(VendasActivity.this);
				layoutManager.setReverseLayout(true);
				layoutManager.setStackFromEnd(true);
				
				//Edição de Pedido
				if (pedidoRealizado != null) {
						itens = new ArrayList<ItemPedido>();
						
						for (ItemPedido aux : pedidoRealizado.getItens()) {
								ItemPedido itensAux = new ItemPedido();
								itensAux.setId(aux.getId());
								itensAux.setQuantidade(aux.getQuantidade());
								itensAux.setValorUnitario(aux.getValorUnitario());
								itensAux.setChavesItemPedido(aux.getChavesItemPedido());
								itensAux.setDescricao(aux.getDescricao());
								itensAux.setValorTotal(aux.getValorTotal());
								itens.add(itensAux);
								
								
						}
						layoutManager = new LinearLayoutManager(VendasActivity.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						adapterItemPedidoAdapter = new ItemPedidoAdapter(this, itens, valorTotalProduto);
						itemPedidoRecyclerView.setLayoutManager(layoutManager);
						itemPedidoRecyclerView.setAdapter(adapterItemPedidoAdapter);
						valorTotalProduto.setText(NumberFormat.getCurrencyInstance().format(pedidoRealizado.getValorTotal()));
						for (int i = 0; i < tiposRecebimento.size(); i++) {
								if (tiposRecebimento.get(i).equals(pedidoRealizado.getTipoRecebimento())) {
										formaPagamentoSpinner.setSelection(i);
										break;
										
								}
						}
				} else {
						//Carrega a lista de itens vazio
						layoutManager = new LinearLayoutManager(VendasActivity.this);
						layoutManager.setReverseLayout(true);
						layoutManager.setStackFromEnd(true);
						itens = new ArrayList<ItemPedido>();
						adapterItemPedidoAdapter = new ItemPedidoAdapter(this, itens, valorTotalProduto);
						itemPedidoRecyclerView.setLayoutManager(layoutManager);
						itemPedidoRecyclerView.setAdapter(adapterItemPedidoAdapter);
						
						formaPagamentoSpinner.setSelection(0);
						formaPagamento = adapterFormaPagamento.getItem(0);
				}
				
				produtos = produtoDAO.carregaProdutos();
				carregaCodigoProdutos(produtos);
				carregaDescricoesProdutos(produtos);
				
				layoutManager = new LinearLayoutManager(VendasActivity.this);
				layoutManager.setReverseLayout(true);
				layoutManager.setStackFromEnd(true);
				
				adaptadorCodigoProduto = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, codigosProdutos);
				codigoProdutoAutoCompleteTextView.setAdapter(adaptadorCodigoProduto);
				codigoProdutoAutoCompleteTextView.setOnItemClickListener(this);
				
				adaptadorDescricaoProduto = new ArrayAdapter(this,
								android.R.layout.simple_list_item_1, descricoesProdutos);
				spnProduto.setAdapter(adaptadorDescricaoProduto);
				
				spnProduto.setOnItemSelectedListener(this);
				
				layoutManager = new LinearLayoutManager(VendasActivity.this);
				layoutManager.setReverseLayout(true);
				layoutManager.setStackFromEnd(true);
				
				codigoProdutoAutoCompleteTextView.setOnClickListener(this);
				confirmarPedidoButton.setOnClickListener(this);
				cancelarPedidoButton.setOnClickListener(this);
				
				precoDAO = PrecoDAO.getInstace();
				unidadeDAO = UnidadeDAO.getInstace();
				uniSpinner.setSelection(0);
				uniSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								
								codigoUnidade = (String) parent.getAdapter().getItem(position);
								preco = precoDAO.carregaPrecoUnidadeProduto(produtoSelecionado, codigoUnidade);
								precoEditText.setText(NumberFormat.getCurrencyInstance().format(preco.getValor()).replace("R$", ""));
								quantidadeAtual = Integer.parseInt(qtdEditText.getText().toString());
								if (quantidadeAtual >= 0) {
										Double preco = null;
										try {
												preco = NumberFormat.getCurrencyInstance().parse("R$" + precoEditText.getText().toString()).doubleValue();
										} catch (ParseException e) {
												e.printStackTrace();
										}
										valorTotal = Integer.parseInt(qtdEditText.getText().toString()) * preco;
										valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
								}
								valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
								
						}
						
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						
						}
				});
				
				addButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								
								if (!qtdEditText.getText().toString().isEmpty() && Integer.parseInt(qtdEditText.getText().toString()) > 0) {
										String qtd = qtdEditText.getText().toString();
										if (valorTotal >= 0 && Integer.parseInt(qtd) >= 1) {
												
												int i = 0;
												
												while (i < itens.size()) {
														
														if (itens.get(i).getDescricao().equals(produtoSelecionado.getNome())) {
																break;
														} else {
																i++;
														}
														
												}
												if (i == itens.size()) {
														//
														addItemPedido(produtoSelecionado, Integer.parseInt(qtdEditText.getText().toString()));
												} else {
														Toast.makeText(VendasActivity.this, "O produto já se encontra na lista de itens do pedido.", Toast.LENGTH_LONG).show();
												}
												
												
										} else {
												qtdEditText.setError("Quantidade Inválida");
												Toast.makeText(VendasActivity.this, "A quantidade minima de produtos deve ser 1!", Toast.LENGTH_LONG).show();
												
										}
										
										
								} else {
										qtdEditText.setError("Quantidade Inválida");
								}
						}
				});
				
				formaPagamentoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								formaPagamento = (String) parent.getAdapter().getItem(position);
						}
						
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						
						}
				});
				
				
		}
		
		private void carregaPositivados() {
				ArrayList<Cliente> clientePositivados = new ArrayList<Cliente>();
				clientePositivados = pedidoDAO.positivados(dataVenda);
				if (clientePositivados != null) {

						for (int i = 0; i < clientePositivados.size(); i++) {
								if (clientePositivados.get(i).getId() == cliente.getId()) {
										//entao carrega o pedido
										pedidoRealizado = pedidoDAO.searchPedido(cliente, dataVenda);
										break;

								}
						}


				}
		}
		
		private void addItemPedido(Produto produto, double quantidade) {
				
				ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstace();
				ItemPedido itemPedido = new ItemPedido();
				
				ItemPedidoID itemPedidoID= new ItemPedidoID();
				
				itemPedidoID.setIdProduto(produto.getId());
				itemPedidoID.setIdUnidade(codigoUnidade);
				
				
				
				SimpleDateFormat formatador = new SimpleDateFormat("yyyy/MM/dd");
				String strData = formatador.format(new Date(System.currentTimeMillis()));
				try {
						itemPedidoID.setDataVenda(formatador.parse(strData));
				} catch (ParseException e) {
						e.printStackTrace();
				}
				
				//Nao sei o significado
				itemPedidoID.setVendaMae("N");
				itemPedidoID.setNucleoCodigo(1);
				itemPedidoID.setTipoVenda("?");
				
				itemPedido.setQuantidade(quantidade);
				try {
						//Converte o edit text do preco unitario e pega sempre o valor digitado
						Number number = NumberFormat.getCurrencyInstance().parse("R$" + precoEditText.getText().toString());
						itemPedido.setValorUnitario(number.doubleValue());
				} catch (ParseException e) {
						e.printStackTrace();
				}
				itemPedido.setDescricao(produto.getNome());
			
				
				try {
						itemPedido.setValorTotal(NumberFormat.getCurrencyInstance().parse(valorTotalTextView.getText().toString()).doubleValue());
				} catch (ParseException e) {
						e.printStackTrace();
				}
				;
				itemPedido.setChavesItemPedido(itemPedidoID);
				long id = itemPedidoDAO.addItemPedido(itemPedido);
				itemPedido.setId(id);
				
				
				adapterItemPedidoAdapter.addListItem(itemPedido, (itens.size() + 1));
				
				valorTotalProduto.setText("VALOR TOTAL: " + NumberFormat.getCurrencyInstance().format(calculaValorTotalPedido(itens)));
		}
		
		private void carregaDescricoesProdutos(ArrayList<Produto> produtos) {
				descricoesProdutos = new ArrayList<String>();
				for (Produto produto : produtos) {
						descricoesProdutos.add(produto.getNome());
				}
		}
		
		private void carregaCodigoProdutos(ArrayList<Produto> produtos) {
				
				codigosProdutos = new ArrayList<String>();
				
				for (Produto produto : produtos) {
						codigosProdutos.add(String.valueOf(produto.getId()));
				}
				
				
		}
		
		private void initView() {
				
				mToolbar = (Toolbar) findViewById(R.id.toolbar);
				mToolbar.setTitle("Vendas");
				setSupportActionBar(mToolbar);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				
				codigoProdutoAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_txt_codigo);
				spnProduto = (Spinner) findViewById(R.id.spn_produtos);
				
				itemPedidoRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_itens);
				confirmarPedidoButton = (Button) findViewById(R.id.btn_confirmar_pedido);
				cancelarPedidoButton = (Button) findViewById(R.id.btn_cancelar_pedido);
				valorTotalTextView = (TextView) findViewById(R.id.txt_valor_total);
				
				qtdBicos = (EditText) findViewById(R.id.edtBicos);
				
				qtdEditText = (EditText) findViewById(R.id.edtQTD);
				precoEditText = (EditText) findViewById(R.id.txt_preco_unitario);
				
				formaPagamentoSpinner = (Spinner) findViewById(R.id.spinner_forma_pagamento);
				
				uniSpinner = (Spinner) findViewById(R.id.spinner_unidade);
				addButton = (Button) findViewById(R.id.button_add);
				
				valorTotalProduto = (TextView) findViewById(R.id.txt_valor_total_pedido);
				valorTotalProduto.setText("VALOR TOTAL: 00,00");
				
				
		}
		
		private Cliente getParams() {
				Bundle args = getIntent().getExtras();
				Cliente cliente = (Cliente) args.getSerializable("keyCliente");
				dataVenda=args.getString("dataVenda");
				return cliente;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
				int id = item.getItemId();
				
				switch (id) {
						
						case android.R.id.home:
								NavUtils.navigateUpFromSameTask(this);
								break;
				}
				
				return super.onOptionsItemSelected(item);
		}
		
		@Override
		public void onClickListener(View view, int position) {
		
		}
		
		@Override
		public void onLongPressClickListener(View view, int position) {
		
		}
		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
				String descricaoProduto = (String) parent.getAdapter().getItem(position);
				for (Produto aux : produtos) {
						if (aux.getNome().equals(descricaoProduto)) {
								
								codigoProdutoAutoCompleteTextView.setText(String.valueOf(aux.getId()));
								
								for (int i = 0; i < descricoesProdutos.size(); i++) {
										if (descricoesProdutos.get(i).equals(aux.getNome())) {
												
												produtoSelecionado = aux;
												showADDProduto();
												break;
										}
								}
						}
				}
				
		}
		
		private void setaSpinner(String codigoProduto) {
				qtdEditText.requestFocus();
				for (Produto aux : produtos) {
						if (aux.getId() == Integer.parseInt(codigoProduto)) {
								
								for (int i = 0; i < descricoesProdutos.size(); i++) {
										if (descricoesProdutos.get(i).equals(aux.getNome())) {
												spnProduto.setSelection(i);
												produtoSelecionado = aux;
												showADDProduto();
												break;
										}
								}
						}
				}
				
				
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				String codigoProduto = (String) parent.getAdapter().getItem(position);
				setaSpinner(codigoProduto);
				
				
		}
		
		@Override
		public void onClick(View v) {
				switch (v.getId()) {
						
						case R.id.btn_confirmar_pedido:
								
								if (itens.size() > 0 && !formaPagamento.equals("FORMAS DE PAGAMENTO")) {
										if (pedidoRealizado != null) {
												updatePedido();
										} else {
												confirmaPedido();
										}
										
								} else if (formaPagamento.equals("FORMAS DE PAGAMENTO")) {
										Toast.makeText(VendasActivity.this, "Forma de Pagamento Inválida!", Toast.LENGTH_LONG).show();
								} else {
										Toast.makeText(VendasActivity.this, "Por favor, adicione no minimo um item na lista!", Toast.LENGTH_LONG).show();
								}
								
								break;
						
						case R.id.btn_cancelar_pedido:
								alertaConfirmacao(VendasActivity.this, "Deseja Realmente Cancelar o Pedido?");
								if (pedidoRealizado != null) {
										pedidoDAO.remove(pedidoRealizado);
								}
								break;
						
						case R.id.auto_txt_codigo:
								
								break;
						
						
				}
		}
		
		private void updatePedido() {
				RealmList<ItemPedido> itensPedidos = new RealmList<ItemPedido>();
				for (int i = 0; i < itens.size(); i++) {
						ItemPedido aux = new ItemPedido();
						
						aux.setDescricao(itens.get(i).getDescricao());
						aux.setValorTotal(itens.get(i).getValorTotal());
						aux.setId(itens.get(i).getId());
						aux.setChavesItemPedido(itens.get(i).getChavesItemPedido());
						aux.setQuantidade(itens.get(i).getQuantidade());
						aux.setValorUnitario(itens.get(i).getValorUnitario());
						itensPedidos.add(aux);
				}
				int codigoFormaPagamento = tipoRecebimentoDAO.codigoFormaPagamento(formaPagamento);
				pedidoRealizado.setValorTotal(calculaValorTotalPedido(itens));
				pedidoRealizado.setItens(itensPedidos);
				pedidoRealizado.setTipoRecebimento(codigoFormaPagamento );
				pedidoDAO.updatePedido(pedidoRealizado);
				//alert de confimacao
				
				Toast.makeText(VendasActivity.this, "Pedido Alterado com Sucesso!", Toast.LENGTH_LONG).show();
				NavUtils.navigateUpFromSameTask(this);
		}
		
		private void confirmaPedido() {
				
				try {
						Double valorTotalPedido;
						
						Pedido pedido = new Pedido();
						
						RealmList<ItemPedido> itensPedidos = new RealmList<ItemPedido>();
						
						int codigoFormaPagamento = tipoRecebimentoDAO.codigoFormaPagamento(formaPagamento);
						
						SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
						String strData = formatador.format(new Date(System.currentTimeMillis()));
						try {
								pedido.setDataPedido(formatador.parse(strData));
						} catch (ParseException e) {
								e.printStackTrace();
						}
						
						for (int i = 0; i < itens.size(); i++) {
								ItemPedido aux = new ItemPedido();
								ItemPedidoID itemPedidoID = itens.get(i).getChavesItemPedido();
								itemPedidoID.setDataVenda(pedido.getDataPedido());
								aux.setDescricao(itens.get(i).getDescricao());
								aux.setValorTotal(itens.get(i).getValorTotal());
								aux.setId(itens.get(i).getId());
								aux.setChavesItemPedido(itemPedidoID);
								aux.setQuantidade(itens.get(i).getQuantidade());
								aux.setValorUnitario(itens.get(i).getValorUnitario());
								itensPedidos.add(aux);
						}
						SessionManager session= new SessionManager(getApplicationContext());
						if(session.checkLogin()){
								finish();
						}
						pedido.setCodigoFuncionario(session.getMatricula());
						pedido.setItens(itensPedidos);
						pedido.setCodigoCliente(cliente.getId());
						pedido.setValorTotal(calculaValorTotalPedido(itens));
						
						pedido.setTipoRecebimento(codigoFormaPagamento);
						pedidoDAO = PedidoDAO.getInstace();
						
						long idVenda=pedidoDAO.addPedido(pedido);
						
						for(ItemPedido itemPedido:itensPedidos){
								ItemPedidoID itemPedidoID=itemPedido.getChavesItemPedido();
								itemPedidoID.setIdVenda(idVenda);
								itemPedido.setChavesItemPedido(itemPedidoID);
								
						}
						
						pedido.setItens(itensPedidos);
						
						pedidoDAO.updatePedido(pedido);
						
						//alert de confimacao
						
						Toast.makeText(VendasActivity.this, "Pedido Realizado com Sucesso!", Toast.LENGTH_LONG).show();
						NavUtils.navigateUpFromSameTask(this);
				} catch (RealmException ex) {
						
						System.out.println("Erro ao inserir:" + ex.getMessage());
				}
				
				
		}
		
		private double calculaValorTotalPedido(ArrayList<ItemPedido> itens) {
				Double valorTotalPedido = 0.0;
				for (int i = 0; i < itens.size(); i++) {
						
						valorTotalPedido += itens.get(i).getValorTotal();
						
						
				}
				return valorTotalPedido;
		}
		
		private void showADDProduto() {
				
				qtdEditText.setText("1");
				quantidadeAtual = 1;
				
				preco = precoDAO.carregaPrecoProduto(produtoSelecionado);
				ArrayList<String> unidades = new ArrayList<String>();
				ArrayList<String> todasUnidades = new ArrayList<String>();
				String unidadePadrao = "";
				unidadePadrao = unidadeDAO.carregaUnidadePadraoProduto(produtoSelecionado);
				unidades.add(unidadePadrao);
				todasUnidades = unidadeDAO.carregaUnidadesProduto(produtoSelecionado);
				unidades.addAll(todasUnidades);
				adapterUnidade = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unidades);
				//adapter da unidade
				uniSpinner.setAdapter(adapterUnidade);
				codigoUnidade = adapterUnidade.getItem(0);
				//unidade clicada seta o preco
				
				valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(quantidadeAtual * preco.getValor()));
				
				// valorTotal = quantidadeAtual * preco.getValor();
				precoEditText.setText(NumberFormat.getCurrencyInstance().format(preco.getValor()).replace("R$", ""));
				qtdEditText.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						
						}
						
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
								
								quantidadeAtual = Double.parseDouble(s.toString().equals("") ? "0" : s.toString());
								if (quantidadeAtual > 0) {
										valorTotal = quantidadeAtual * Double.parseDouble(precoEditText.getText().toString().replace(",", "."));
										
										valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
										
								}
						}
						
						@Override
						public void afterTextChanged(Editable s) {
								valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
						}
				});
				
				precoEditText.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						
						}
						
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
								
								if (quantidadeAtual > 0) {
										Double preco = null;
										try {
												preco = NumberFormat.getCurrencyInstance().parse("R$" + precoEditText.getText().toString()).doubleValue();
										} catch (ParseException e) {
												e.printStackTrace();
										}
										valorTotal = Integer.parseInt(qtdEditText.getText().toString()) * preco;
										valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
								}
								valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(valorTotal));
						}
						
						@Override
						public void afterTextChanged(Editable s) {
						}
				});
				
		}
		
		public void alertaConfirmacao(Context ctx, String strMsg) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setTitle("Cancelamento de Pedido");
				builder.setMessage(strMsg);
				
				String positiveText = "SIM";
				builder.setPositiveButton(positiveText,
								new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
												// positive button logic
												
												finish();
												dialog.dismiss();
												//edtConfirmaEmail.setText("");
												
												return;
										}
								});
				
				String negativeText = "NÃO";
				builder.setNegativeButton(negativeText,
								new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
												// negative button logic
												dialog.dismiss();
												return;
										}
								});
				
				AlertDialog dialog = builder.create();
				// display dialog
				dialog.show();
		}
		
		
}

