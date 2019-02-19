package com.br.minasfrango.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.minasfrango.R;
import com.br.minasfrango.dao.PedidoDAO;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Recebimento;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecebimentoAdapter extends RecyclerView.Adapter<RecebimentoAdapter.MyViewHolder> {
		private Context mContext;
		private List<Recebimento> mList;
		private LayoutInflater mLayoutInflater;
		private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
	
		
		public RecebimentoAdapter(Context c, List<Recebimento> l) {
				this.mContext = c;
				this.mList = l;
				this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				
				
		}
		@Override
		public RecebimentoAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
				View v = mLayoutInflater.inflate(R.layout.recebimento_item, viewGroup, false);
				RecebimentoAdapter.MyViewHolder mvh = new RecebimentoAdapter.MyViewHolder(v);
				return mvh;
		}
		
		
		public Recebimento getItem(int position) {
				return this.mList.get(position);
		}
		
		@Override
		public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
				SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
				myViewHolder.idRecebimentoTextView.setText(String.valueOf(mList.get(position).getId()));
				myViewHolder.idVendaTextView.setText(String.valueOf(mList.get(position).getIdVenda()));
				myViewHolder.dataVendaTextView.setText(String.valueOf(formatador.format(mList.get(position).getDataVenda())));
				myViewHolder.valorVendaTextView.setText((NumberFormat.getCurrencyInstance().format(mList.get(position).getValorVenda())));
				
				myViewHolder.valorAmortizacao.setText((NumberFormat.getCurrencyInstance().format(mList.get(position).getValorAmortizado())));
				double saldo=mList.get(position).getValorAmortizado()-mList.get(position).getValorVenda();
				if(saldo<0){
						myViewHolder.saldoTextView.setTextColor(Color.RED);
				}
				else {
						myViewHolder.saldoTextView.setTextColor(Color.BLUE);
				}
				myViewHolder.saldoTextView.setText(NumberFormat.getCurrencyInstance().format(saldo));
			
		}
		
		@Override
		public int getItemCount() {
				return mList.size();
		}
		
		public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
				mRecyclerViewOnClickListenerHack = r;
		}
		
		public void addListItem(Recebimento c, int position) {
				mList.add(c);
				notifyItemInserted(position);
		}
		public void updateListItem(Recebimento c, int position) {
				mList.set(position, c);
				notifyItemInserted(position);
				notifyDataSetChanged();
		}
		public void removeListItem(int position) {
				mList.remove(position);
				notifyItemRemoved(position);
		}
		
		public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
				
				public TextView idRecebimentoTextView,saldoTextView, idVendaTextView,dataVendaTextView,valorVendaTextView,valorAmortizacao;
				
				View view;
				public MyViewHolder(View itemView) {
						super(itemView);
						
						idRecebimentoTextView = (TextView) itemView.findViewById(R.id.txt_id_recebimento);
						idVendaTextView = (TextView) itemView.findViewById(R.id.txt_id_venda);
						dataVendaTextView = (TextView) itemView.findViewById(R.id.txt_data_venda);
						valorVendaTextView = (TextView) itemView.findViewById(R.id.txt_valor_venda);
						valorAmortizacao = (TextView) itemView.findViewById(R.id.txt_amortizado);
						saldoTextView=(TextView) itemView.findViewById(R.id.txt_saldo);
						
						view = (View) itemView.findViewById(R.id.view_linha);
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
}
