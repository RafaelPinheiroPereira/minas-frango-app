package com.br.minasfrango.data.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.listener.RecyclerViewOnClickListenerHack;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

        myViewHolder.dataVendaTextView.setText(String.valueOf(formatador.format(mList.get(position).getDataVenda())));
        myViewHolder.dataVencimentoTextView
                .setText(String.valueOf(formatador.format(mList.get(position).getDataVencimento())));

        myViewHolder.valorVendaTextView
                .setText((NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(mList.get(position).getValorVenda())));

        myViewHolder.valorAmortizacao
                .setText((NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(mList.get(position).getValorAmortizado())));
        double saldo =  mList.get(position).getValorVenda()- mList.get(position).getValorAmortizado();
        if (saldo > 0) {
            myViewHolder.saldoTextView.setTextColor(Color.RED);
        } else {
            myViewHolder.saldoTextView.setTextColor(Color.GREEN);
        }
        myViewHolder.saldoTextView.setText((NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(saldo)));
        myViewHolder.qtdTextView.setText(String.valueOf(position+1)+"/"+String.valueOf(mList.size()));

        myViewHolder.mCheckBox.setChecked(mList.get(position).isCheck());




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

    public void updateAmortizacao(Recebimento c, int position) {
        c.setCheck(true);
        mList.set(position, c);
        //notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void updateRetiraAmortizacao(Recebimento c, int position) {
        c.setCheck(false);
        mList.set(position, c);
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
             implements View.OnClickListener, View.OnLongClickListener {

        CheckBox mCheckBox;

        TextView idRecebimentoTextView,
                saldoTextView,
                qtdTextView, dataVendaTextView, valorVendaTextView, valorAmortizacao, dataVencimentoTextView;


        public MyViewHolder(View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.chk_recebimento);
            idRecebimentoTextView = itemView.findViewById(R.id.txt_id_recebimento);
            qtdTextView=itemView.findViewById(R.id.txt_qtd);

            dataVendaTextView = itemView.findViewById(R.id.txt_data_venda);
            valorVendaTextView = itemView.findViewById(R.id.txt_valor_venda);
            valorAmortizacao = itemView.findViewById(R.id.txt_amortizado);
            saldoTextView = itemView.findViewById(R.id.txt_saldo);
            dataVencimentoTextView = itemView.findViewById(R.id.txt_data_vencimento);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mCheckBox.setOnClickListener(this);


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
