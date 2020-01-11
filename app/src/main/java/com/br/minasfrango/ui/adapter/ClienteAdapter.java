package com.br.minasfrango.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.ui.listener.RecyclerViewOnClickListenerHack;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 04717299302 on 15/12/2016.
 */

public class  ClienteAdapter
        extends RecyclerView.Adapter<ClienteAdapter.MyViewHolder> implements Filterable {

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.btnReceber)
        public Button btnReceber;

        public TextView txtAdress;

        public TextView txtNameFantasy;

        public TextView txtIdCliente;

        @BindView(R.id.imgInfo)
        ImageView imgInfo;

        @BindView(R.id.btnVender)
        Button btnVender;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            txtNameFantasy = itemView.findViewById(R.id.textViewNomeFantasia);
            txtAdress = itemView.findViewById(R.id.textViewEndereco);
            txtIdCliente=itemView.findViewById(R.id.textViewIdCliente);

            btnVender.setOnClickListener(this);
            btnReceber.setOnClickListener(this);
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

    private List<Cliente> mClienteListFiltered;

    private LayoutInflater mLayoutInflater;

    private List<Cliente> mList;

    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public ClienteAdapter(Context c, List<Cliente> l) {

        this.mList = l;
        this.mClienteListFiltered = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    public void addListItem(Cliente c, int position) {
        mClienteListFiltered.add(c);
        notifyItemInserted(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mClienteListFiltered = mList;
                } else {
                    List<Cliente> filteredList = new ArrayList<>();
                    for (Cliente clienteORM : mList) {

                        if (clienteORM.getNome().toLowerCase().contains(charString.toLowerCase()) || String.valueOf(clienteORM.getId()).contains(charSequence)) {
                            filteredList.add(clienteORM);
                        }
                    }

                    mClienteListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mClienteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mClienteListFiltered = (ArrayList<Cliente>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Cliente getItem(int position) {
        return this.mClienteListFiltered.get(position);
    }

    @Override
    public int getItemCount() {
        return mClienteListFiltered.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        myViewHolder.txtIdCliente.setText(String.format("%05d",mClienteListFiltered.get(position).getId()));
        myViewHolder.txtNameFantasy.setText(mClienteListFiltered.get(position).getNome());
        myViewHolder.txtAdress.setText(
                mClienteListFiltered.get(position).getEndereco() != null ? mClienteListFiltered.get(position)
                        .getEndereco() : "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_recycle_home, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    public void removeListItem(int position) {
        mClienteListFiltered.remove(position);
        notifyItemRemoved(position);
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }


}
