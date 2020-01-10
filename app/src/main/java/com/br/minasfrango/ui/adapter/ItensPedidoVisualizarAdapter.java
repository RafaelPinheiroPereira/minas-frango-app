package com.br.minasfrango.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.ItemPedido;
import java.text.NumberFormat;
import java.util.List;

public class ItensPedidoVisualizarAdapter
        extends RecyclerView.Adapter<ItensPedidoVisualizarAdapter.ItemViewHolder> {

    private List<ItemPedido> mItensPedido;

    private LayoutInflater mLayoutInflater;

    public ItensPedidoVisualizarAdapter(Context c, List<ItemPedido> l) {

        this.mItensPedido = l;

        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtQTDBicos,
                descricaoTextView,
                valorTextView,
                unidadeTextView,
                qtdTextView,
                 txtLote,
                vlrUnitarioTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            descricaoTextView = itemView.findViewById(R.id.descricao_text_view_item_visualizar);
            txtQTDBicos = itemView.findViewById(R.id.txtQTDBicos);
            valorTextView = itemView.findViewById(R.id.valor_total_txt_item_visualizar);
            unidadeTextView = itemView.findViewById(R.id.unidade_txt_item_visualizar);
            qtdTextView = itemView.findViewById(R.id.qtd_text_view_item_visualizar);
            vlrUnitarioTextView = itemView.findViewById(R.id.valor_unitario_txt_item_visualizar);
            txtLote= itemView.findViewById(R.id.txtLote);
        }
    }

    @Override
    public int getItemCount() {
        return mItensPedido.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, final int position) {
        final ItemPedido itemPedido = mItensPedido.get(position);

        itemViewHolder.descricaoTextView.setText((itemPedido.getDescricao()));
        itemViewHolder.unidadeTextView.setText(itemPedido.getChavesItemPedido().getIdUnidade());
        itemViewHolder.qtdTextView.setText(String.format("%.2f",itemPedido.getQuantidade()));
        itemViewHolder.valorTextView.setText(
                NumberFormat.getCurrencyInstance().format(itemPedido.getValorTotal()));
        itemViewHolder.vlrUnitarioTextView.setText(
                NumberFormat.getCurrencyInstance().format(itemPedido.getValorUnitario()));

        itemViewHolder.txtQTDBicos.setText(String.valueOf(itemPedido.getBicos()));
        itemViewHolder.txtLote.setText(itemPedido.getLote());

    }

    @Override
    public ItensPedidoVisualizarAdapter.ItemViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_pedido_visualizar, viewGroup, false);
        ItensPedidoVisualizarAdapter.ItemViewHolder mvh =
                new ItensPedidoVisualizarAdapter.ItemViewHolder(v);
        return mvh;
    }
}
