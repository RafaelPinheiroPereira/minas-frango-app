package com.br.minasfrango.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.ui.listener.RecyclerViewOnClickListenerHack;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 15/12/2016.
 */

public class ClienteAdapter
        extends RecyclerView.Adapter<ClienteAdapter.MyViewHolder> implements Filterable {

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.btnReceber)
        public ImageView imgReceber;

        public TextView txtAdress;

        public TextView txtNameFantasy;

        @BindView(R.id.imgInfo)
        ImageView imgInfo;

        @BindView(R.id.btnVender)
        ImageView imgVender;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            txtNameFantasy = itemView.findViewById(R.id.textViewNomeFantasia);
            txtAdress = itemView.findViewById(R.id.textViewEndereco);

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

    private List<Cliente> clienteListFiltered;

    private LayoutInflater mLayoutInflater;

    private List<Cliente> mList;

    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public ClienteAdapter(Context c, List<Cliente> l) {

        this.mList = l;
        this.clienteListFiltered = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    public void addListItem(Cliente c, int position) {
        clienteListFiltered.add(c);
        notifyItemInserted(position);
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

    public Cliente getItem(int position) {
        return this.clienteListFiltered.get(position);
    }

    @Override
    public int getItemCount() {
        return clienteListFiltered.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.txtNameFantasy.setText(clienteListFiltered.get(position).getNome());
        myViewHolder.txtAdress.setText(
                clienteListFiltered.get(position).getEndereco() != null ? clienteListFiltered.get(position)
                        .getEndereco() : "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.cliente_item_new, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    public void removeListItem(int position) {
        clienteListFiltered.remove(position);
        notifyItemRemoved(position);
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }


}
