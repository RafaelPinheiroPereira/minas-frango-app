package com.br.minasfrango.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.ItemPedido;
import com.br.minasfrango.ui.mvp.venda.IVendaMVP;
import com.br.minasfrango.util.FormatacaoMoeda;

/**
 * Created by 04717299302 on 13/01/2017.
 */

public class ItemPedidoAdapter extends RecyclerView.Adapter<ItemPedidoAdapter.MyViewHolder> {


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDescricao, txtValorUnitario, txtUnidade, txtQuantidade, txtValorTotal,txtBico;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtDescricao = itemView.findViewById(R.id.txtProductName);
            txtUnidade = itemView.findViewById(R.id.txtUnit);
            txtQuantidade = itemView.findViewById(R.id.txtQTDProducts);
            txtValorTotal = itemView.findViewById(R.id.txt_valor_total_item);
            txtValorUnitario = itemView.findViewById(R.id.txtValorUnitario);
            txtBico=itemView.findViewById(R.id.txt_bicos);


        }


    }

    private LayoutInflater mLayoutInflater;

    private IVendaMVP.IPresenter mPresenter;

    public ItemPedidoAdapter(IVendaMVP.IPresenter presenter) {
        this.mPresenter = presenter;
        this.mLayoutInflater = (LayoutInflater) presenter.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getItemCount() {
        return mPresenter.getItens().size();
    }

    @Override
    public void onBindViewHolder(final ItemPedidoAdapter.MyViewHolder myViewHolder, int position) {
        final ItemPedido itemPedido = mPresenter.getItens().get(position);

        myViewHolder.txtDescricao.setText((itemPedido.getDescricao()));
        myViewHolder.txtUnidade.setText(itemPedido.getChavesItemPedido().getIdUnidade().split("-")[0]);
        myViewHolder.txtQuantidade.setText(String.format("%.2f",itemPedido.getQuantidade()));
        myViewHolder.txtValorUnitario.setText(FormatacaoMoeda.converterParaReal(itemPedido.getValorUnitario()));
        myViewHolder.txtValorTotal
                .setText(FormatacaoMoeda.converterParaReal(itemPedido.getValorTotal()));
        myViewHolder.txtBico.setText(String.valueOf(itemPedido.getBicos()));


    }


    @Override
    public ItemPedidoAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_pedido, viewGroup, false);
        ItemPedidoAdapter.MyViewHolder mvh = new ItemPedidoAdapter.MyViewHolder(v);
        return mvh;


    }


}