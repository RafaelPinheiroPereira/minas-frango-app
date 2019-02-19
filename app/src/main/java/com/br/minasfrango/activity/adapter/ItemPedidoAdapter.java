package com.br.minasfrango.activity.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.br.minasfrango.R;
import com.br.minasfrango.dao.ItemPedidoDAO;
import com.br.minasfrango.dao.PrecoDAO;
import com.br.minasfrango.dao.UnidadeDAO;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Preco;
import com.br.minasfrango.model.Produto;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by 04717299302 on 13/01/2017.
 */

public class ItemPedidoAdapter extends RecyclerView.Adapter<ItemPedidoAdapter.MyViewHolder> implements View.OnClickListener {
		
		private Context mContext;
		private ArrayList<ItemPedido> mList;
		private LayoutInflater mLayoutInflater;
		private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
		ItemPedidoDAO itemPedidoDAO;
		TextView txtValorTotal;
		UnidadeDAO unidadeDAO;
		private ArrayAdapter<String> adapterUnidade;
		String codigoUnidade;
		private Preco preco;
		PrecoDAO precoDAO;
		Double valorTotal;
		int posicao;
		
		public ItemPedidoAdapter(Context c, ArrayList<ItemPedido> l, TextView valorTotal) {
				this.mContext = c;
				this.mList = l;
				this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemPedidoDAO = ItemPedidoDAO.getInstace();
				txtValorTotal = valorTotal;
				
				
		}
		
		@Override
		public ItemPedidoAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
				
				
				View v = mLayoutInflater.inflate(R.layout.item_pedido, viewGroup, false);
				ItemPedidoAdapter.MyViewHolder mvh = new ItemPedidoAdapter.MyViewHolder(v);
				return mvh;
			
				
				
		}
		
		@Override
		public void onBindViewHolder(final ItemPedidoAdapter.MyViewHolder myViewHolder, int position) {
				final ItemPedido itemPedido = mList.get(position);
				posicao = position;
				
				myViewHolder.idTextView.setText(String.valueOf(itemPedido.getChavesItemPedido().getIdProduto()));
				myViewHolder.descricaoTextView.setText((itemPedido.getDescricao()));
				myViewHolder.unidadeTextView.setText(itemPedido.getChavesItemPedido().getIdUnidade());
				myViewHolder.qntdTextView.setText(String.valueOf(itemPedido.getQuantidade()));
				myViewHolder.valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(itemPedido.getValorTotal()));
				
				myViewHolder.btnExcluir.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								itemPedidoDAO.removeItemPedido(itemPedido);
								removeListItem(posicao);
								txtValorTotal.setText("Totalizador" + NumberFormat.getCurrencyInstance().format(calculaValorTotalPedido(mList)));
								
						}
				});
				
				myViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								initDialog(itemPedido, posicao, ((MyViewHolder) myViewHolder).valorTotalTextView, txtValorTotal);
								
								
						}
				});
				
				
		}
		
		@Override
		public int getItemCount() {
				return mList.size();
		}
		
		public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
				mRecyclerViewOnClickListenerHack = r;
		}
		
		public void addListItem(ItemPedido c, int position) {
				mList.add(c);
				notifyItemInserted(position);
		}
		
		public void updateListItem(ItemPedido c, int position) {
				mList.set(position, c);
				notifyItemInserted(position);
				notifyDataSetChanged();
		}
		
		public void removeListItem(int position) {
				mList.remove(position);
				notifyItemRemoved(position);
		}
		
		@Override
		public void onClick(View view) {
				
		}
		
		public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
				public TextView descricaoTextView;
				public TextView idTextView;
				public TextView unidadeTextView;
				public TextView qntdTextView;
				public TextView valorTotalTextView;
				private Button btnExcluir, btnEdit;
				
				public MyViewHolder(View itemView) {
						super(itemView);
						
						descricaoTextView = (TextView) itemView.findViewById(R.id.textViewDescricaoItem);
						idTextView = (TextView) itemView.findViewById(R.id.textViewID);
						
						unidadeTextView = (TextView) itemView.findViewById(R.id.textViewUnidade);
						qntdTextView = (TextView) itemView.findViewById(R.id.textViewQTDItem);
						
						valorTotalTextView = (TextView) itemView.findViewById(R.id.textValorTotal);
						
						btnExcluir = (Button) itemView.findViewById(R.id.btn_delete_item_pedido);
						btnEdit = (Button) itemView.findViewById(R.id.btn_edit_item_pedido);
						
						itemView.setOnClickListener(this);
						itemView.setOnLongClickListener(this);
				}
				
				@Override
				public void onClick(View v) {
						if (mRecyclerViewOnClickListenerHack != null) {
								mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
						}
				}
				
				@Override
				public boolean onLongClick(View v) {
						if (mRecyclerViewOnClickListenerHack != null) {
								mRecyclerViewOnClickListenerHack.onLongPressClickListener(v, getPosition());
								return true;
						}
						return false;
				}
		}
		
		private double calculaValorTotalPedido(ArrayList<ItemPedido> itens) {
				Double valorTotalPedido = 0.0;
				for (int i = 0; i < itens.size(); i++) {
						
						valorTotalPedido += itens.get(i).getValorTotal();
						
						
				}
				return valorTotalPedido;
		}
		
		private void initDialog(final ItemPedido itemPedido, final int position, final TextView txtValorTotal, final TextView totalizador) {
				final Dialog dialog = new Dialog(mContext);
				
				TextView idProdutoTextView, descricaoProdutoTextView;
				final EditText quantidadeEditText, precoEditText;
				Spinner unidadeSpinner = null;
				Button cancelarEdicaoButton, salvaEdicaoButton;
				ArrayList<String> unidades = new ArrayList<String>();
				ArrayList<String> todasUnidades = new ArrayList<String>();
				String unidadePadrao = "";
				
				dialog.setContentView(R.layout.dialog_edit_item_pedido);
				
				idProdutoTextView = (TextView) dialog.findViewById(R.id.txt_codigo_produto);
				descricaoProdutoTextView = (TextView) dialog.findViewById(R.id.txt_descricao_produto);
				quantidadeEditText = (EditText) dialog.findViewById(R.id.edt_quantidade_produto);
				precoEditText = (EditText) dialog.findViewById(R.id.edt_preco);
				unidadeSpinner = (Spinner) dialog.findViewById(R.id.spinner_unidade_produto);
				cancelarEdicaoButton = (Button) dialog.findViewById(R.id.button_cancelar_edicao);
				salvaEdicaoButton = (Button) dialog.findViewById(R.id.button_confirmar_edicao);
				
				// Custom Android Allert Dialog Title
				dialog.setTitle("Edição Item Pedido");
				
				idProdutoTextView.setText(String.valueOf(itemPedido.getChavesItemPedido().getIdProduto()));
				descricaoProdutoTextView.setText(itemPedido.getDescricao());
				precoEditText.setText(NumberFormat.getCurrencyInstance().format(itemPedido.getValorUnitario()).replace("R$", ""));
				quantidadeEditText.setText(String.valueOf(itemPedido.getQuantidade()));
				
				unidadeDAO = UnidadeDAO.getInstace();
				precoDAO = PrecoDAO.getInstace();
				final Produto produto = new Produto();
				produto.setId((long) itemPedido.getChavesItemPedido().getIdProduto());
				produto.setNome(itemPedido.getDescricao());
				unidadePadrao = unidadeDAO.carregaUnidadePadraoProduto(produto);
				unidades.add(unidadePadrao);
				todasUnidades = unidadeDAO.carregaUnidadesProduto(produto);
				unidades.addAll(todasUnidades);
				adapterUnidade = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, unidades);
				//adapter da unidade
				unidadeSpinner.setAdapter(adapterUnidade);
				codigoUnidade = adapterUnidade.getItem(0);
				
				unidadeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								
								codigoUnidade = (String) parent.getAdapter().getItem(position);
								preco = precoDAO.carregaPrecoUnidadeProduto(produto, codigoUnidade);
								precoEditText.setText(NumberFormat.getCurrencyInstance().format(preco.getValor()).replace("R$", ""));
								
						}
						
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						
						}
				});
				
				cancelarEdicaoButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								dialog.dismiss();
						}
				});
				salvaEdicaoButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								//chama o edit no item pedido dao e chama edit na lista de itens
								
								ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstace();
								
								itemPedido.setQuantidade(Integer.parseInt(quantidadeEditText.getText().toString()));
								try {
										//Converte o edit text do preco unitario e pega sempre o valor digitado
										Number number = NumberFormat.getCurrencyInstance().parse("R$" + precoEditText.getText().toString());
										itemPedido.setValorUnitario(number.doubleValue());
								} catch (ParseException e) {
										e.printStackTrace();
								}
								
								ItemPedidoAdapter.this.valorTotal = 0.0;
								int quantidadeAtual = Integer.parseInt(quantidadeEditText.getText().toString());
								if (quantidadeAtual >= 0) {
										Double preco = null;
										try {
												preco = NumberFormat.getCurrencyInstance().parse("R$" + precoEditText.getText().toString()).doubleValue();
										} catch (ParseException e) {
												e.printStackTrace();
										}
										ItemPedidoAdapter.this.valorTotal = (quantidadeAtual * preco);
										txtValorTotal.setText(NumberFormat.getCurrencyInstance().format(ItemPedidoAdapter.this.valorTotal));
										
								}
								
								itemPedido.setValorTotal(ItemPedidoAdapter.this.valorTotal);
								itemPedido.setDescricao(produto.getNome());
								itemPedido.getChavesItemPedido().setIdUnidade(codigoUnidade);
								itemPedidoDAO.updateItem(itemPedido);
								
								updateListItem(itemPedido, position);
								totalizador.setText("Totalizador" + NumberFormat.getCurrencyInstance().format(calculaValorTotalPedido(mList)));
								
								dialog.dismiss();
								Toast.makeText(mContext, "Item alterado com sucesso", Toast.LENGTH_LONG).show();
						}
				});
				dialog.show();
				
		}
		
	
		
		
		
		
}
