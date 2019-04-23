package com.br.minasfrango.ui.views;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.ui.listener.ClickSubItemListener;
import java.text.DateFormat;
import java.text.NumberFormat;

public class PedidosViewHolder extends ChildViewHolder implements OnClickListener {

    private Object pedido;

    private TextView valorTotalTextView, idTextView, dataTextView;

    private ImageView menuImageView;

    private ClickSubItemListener mClickSubItemListener;

    public PedidosViewHolder(View itemView) {
        super(itemView);
        valorTotalTextView = itemView.findViewById(R.id.txt_valor_pedido_child);
        idTextView = itemView.findViewById(R.id.txt_id_pedido_child);
        dataTextView = itemView.findViewById(R.id.txt_data_pedido_child);
        menuImageView = itemView.findViewById(R.id.menu_imagem_view);
        itemView.setOnClickListener(this);
        menuImageView.setOnClickListener(this);

    }


    public void bind(final Pedido pedidos) {
        valorTotalTextView.setText(NumberFormat.getCurrencyInstance().format(pedidos.getValorTotal()));
        idTextView.setText(String.valueOf(pedidos.getId()));
        dataTextView.setText(DateFormat.getDateInstance().format(pedidos.getDataPedido()).toUpperCase());
        if (pedidos.isCancelado()) {
            valorTotalTextView.setTextColor(Color.RED);
            idTextView.setTextColor(Color.RED);
            dataTextView.setTextColor(Color.RED);
        }
        pedido = pedidos;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.menu_imagem_view:
                if (mClickSubItemListener != null) {
                    mClickSubItemListener.onClickSubitem(view, pedido);
                }
                break;
        }

    }

    public void setClickSubItemListener(ClickSubItemListener clickSubItemListener) {
        mClickSubItemListener = clickSubItemListener;
    }


}
