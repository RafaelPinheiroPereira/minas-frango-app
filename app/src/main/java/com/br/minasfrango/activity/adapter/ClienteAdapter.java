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
    
    ArrayList<Cliente> positivados;


    public ClienteAdapter(Context c, List<Cliente> l) {
        this.mContext = c;
        this.mList = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.cliente_item_view, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    public  Cliente getItem(int position){
        return  this.mList.get(position);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder,  final int position) {

       


        myViewHolder.razaoSocialTextView.setText(mList.get(position).getRazaoSocial());
        myViewHolder.idClienteTextView.setText(String.valueOf(mList.get(position).getId()));
        myViewHolder.CPFCNPJTextView.setText(mList.get(position).getCpf());
        myViewHolder.nomeFantasiaTextView.setText(mList.get(position).getNome());
        myViewHolder.cidadeTextView.setText(mList.get(position).getCidade()!=null?mList.get(position).getCidade():"");
        myViewHolder.bairroTextView.setText(mList.get(position).getBairro()!=null?mList.get(position).getBairro():"");


}

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(Cliente c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView razaoSocialTextView;
        public TextView idClienteTextView;
        public TextView  CPFCNPJTextView;
        public Button btnReceber,btnVender;
        private LinearLayout linearLayout;
        View view;
        public TextView  nomeFantasiaTextView,cidadeTextView,bairroTextView;




        public MyViewHolder(View itemView) {
            super(itemView);

            razaoSocialTextView = (TextView) itemView.findViewById(R.id.textViewRazaoSocial);
            idClienteTextView = (TextView) itemView.findViewById(R.id.textViewCodigoCliente);

            CPFCNPJTextView = (TextView) itemView.findViewById(R.id.textCPFCNPJ);
            nomeFantasiaTextView = (TextView) itemView.findViewById(R.id.textViewNomeFantasia);

            cidadeTextView= (TextView) itemView.findViewById(R.id.textViewCidade);
            bairroTextView=  (TextView) itemView.findViewById(R.id.textViewBairro);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.layout_cliente);
            view=(View) itemView.findViewById(R.id.view_linha);
            btnReceber= (Button) itemView.findViewById(R.id.btnReceber);
            btnVender=(Button) itemView.findViewById(R.id.btnVender);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            btnVender.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onLongPressClickListener(v, getPosition());
                return  true;
            }
            return false;
        }
    }


}
