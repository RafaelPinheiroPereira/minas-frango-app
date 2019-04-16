package com.br.minasfrango.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.listener.RecyclerViewOnClickListenerHack;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 15/12/2016.
 */

public class ClienteAdapter
				extends RecyclerView.Adapter<ClienteAdapter.MyViewHolder> implements Filterable {
		private Context mContext;
		private List<Cliente> mList;
		private LayoutInflater mLayoutInflater;
		private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
		PedidoDAO pedidoDAO;
		private List<Cliente> clienteListFiltered;
		private ClienteAdapterListener listener;
		
		public ClienteAdapter(Context c, List<Cliente> l) {
				this.mContext = c;
				this.mList = l;
				this.clienteListFiltered=l;
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
				return this.clienteListFiltered.get(position);
		}
		
		@Override
		public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
				
				myViewHolder.nomeFantasiaTextView.setText(clienteListFiltered.get(position).getNome());
				myViewHolder.enderecoTextView.setText(clienteListFiltered.get(position).getEndereco() != null ? clienteListFiltered.get(position).getEndereco() : "");
				
		}
		
		@Override
		public int getItemCount() {
				return clienteListFiltered.size();
		}
		
		public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
				mRecyclerViewOnClickListenerHack = r;
		}
		
		public void addListItem(Cliente c, int position) {
				clienteListFiltered.add(c);
				notifyItemInserted(position);
		}
		
		public void removeListItem(int position) {
				clienteListFiltered.remove(position);
				notifyItemRemoved(position);
		}
		
		@Override
		public Filter getFilter() {
				return new Filter() {
						@Override
						protected FilterResults performFiltering(CharSequence charSequence) {
								String charString = charSequence.toString();
								if (charString.isEmpty()) {
										clienteListFiltered = mList;
								} else {
										List<Cliente> filteredList = new ArrayList<>();
										for (Cliente cliente : mList) {
												
												if (cliente.getNome().toLowerCase().contains(charString.toLowerCase())) {
														filteredList.add(cliente);
												}
										}
										
										clienteListFiltered = filteredList;
								}
								
								FilterResults filterResults = new FilterResults();
								filterResults.values = clienteListFiltered;
								return filterResults;
						}
						
						@Override
						protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
								clienteListFiltered = (ArrayList<Cliente>) filterResults.values;
								notifyDataSetChanged();
						}
				};
		}
		public interface ClienteAdapterListener {
				void onClienteSelected(Cliente cliente);
		}
		
		public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
				
				public ImageView imgReceber, imgVender, imgInfo;
				
				public TextView nomeFantasiaTextView, enderecoTextView;
				
				public MyViewHolder(View itemView) {
						super(itemView);
						
						nomeFantasiaTextView = itemView.findViewById(R.id.textViewNomeFantasia);
						enderecoTextView = itemView.findViewById(R.id.textViewEndereco);
						imgReceber = itemView.findViewById(R.id.btnReceber);
						imgVender = itemView.findViewById(R.id.btnVender);
						imgInfo = itemView.findViewById(R.id.imgInfo);
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
