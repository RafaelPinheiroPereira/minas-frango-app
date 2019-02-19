package com.br.minasfrango.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.minasfrango.R;
import com.br.minasfrango.dao.PedidoDAO;
import com.br.minasfrango.model.Cliente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 15/12/2016.
 */

public class ClienteAdapter
				extends RecyclerView.Adapter<ClienteAdapter.MyViewHolder> {
		private Context mContext;
		private List<Cliente> mList;
		private LayoutInflater mLayoutInflater;
		private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
		PedidoDAO pedidoDAO;

		
		
		
		public ClienteAdapter(Context c, List<Cliente> l) {
				this.mContext = c;
				this.mList = l;

				this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  pedidoDAO = PedidoDAO.getInstace();
				
				
		}
		
		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
				View v = mLayoutInflater.inflate(R.layout.cliente_item_new, viewGroup, false);
				MyViewHolder mvh = new MyViewHolder(v);
				return mvh;
		}
		
		public Cliente getItem(int position) {
				return this.mList.get(position);
		}
		
		@Override
		public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
				
			
				myViewHolder.nomeFantasiaTextView.setText(mList.get(position).getNome());
				myViewHolder.enderecoTextView.setText(mList.get(position).getEndereco() != null ? mList.get(position).getEndereco() : "");
		
		}
		
		@Override
		public int getItemCount() {
				return mList.size();
		}
		
		public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
				mRecyclerViewOnClickListenerHack = r;
		}
		
		public void addListItem(Cliente c, int position) {
				mList.add(c);
				notifyItemInserted(position);
		}
		
		public void removeListItem(int position) {
				mList.remove(position);
				notifyItemRemoved(position);
		}
		
		public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
				
				
				
				public ImageView imgReceber, imgVender,imgInfo;
			
				public TextView nomeFantasiaTextView, enderecoTextView;
				
				public MyViewHolder(View itemView) {
						super(itemView);
						

						
						nomeFantasiaTextView = (TextView) itemView.findViewById(R.id.textViewNomeFantasia);
						enderecoTextView = (TextView) itemView.findViewById(R.id.textViewEndereco);
						imgReceber = (ImageView) itemView.findViewById(R.id.btnReceber);
						imgVender = (ImageView) itemView.findViewById(R.id.btnVender);
						imgInfo = (ImageView) itemView.findViewById(R.id.imgInfo);
						itemView.setOnClickListener(this);
						itemView.setOnLongClickListener(this);
						imgVender.setOnClickListener(this);
						imgReceber.setOnClickListener(this);
						imgInfo.setOnClickListener(this);
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
