package com.br.minasfrango.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.br.minasfrango.R;
import com.br.minasfrango.activity.PedidoActivity;
import com.br.minasfrango.activity.VendasActivity;
import com.br.minasfrango.activity.VisualizarPedidoActivity;
import com.br.minasfrango.dao.PedidoDAO;
import com.br.minasfrango.listener.ClickSubItemListener;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.ClientePedido;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.util.ParentListItem;
import com.br.minasfrango.view.ClientePedidoViewHolder;
import com.br.minasfrango.view.PedidosViewHolder;
import java.util.List;

public class ClientePedidoAdapter extends ExpandableRecyclerAdapter<ClientePedidoViewHolder, PedidosViewHolder>
        implements ClickSubItemListener, OnClickListener {

    private LayoutInflater mInflator;

    private Context mContext;
    Pedido mPedidoSelecionado;


    public ClientePedidoAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public void onClick( View view) {

        switch (view.getId()){
            case R.id.view_ll_bottom_sheet:
                visualizarPedido(mPedidoSelecionado);
                break;
            case R.id.edit_ll_bottom_sheet:
                 editarPedido(mPedidoSelecionado);
                break;
            case R.id.delete_ll_bottom_sheet:
                deletarPedido(mPedidoSelecionado);
                break;
        }

    }


    @Override
    public void onClickSubitem(final View v, final Object o) {
        mPedidoSelecionado= (Pedido) o;
        showBottomSheetDialog();

    }

    private void showBottomSheetDialog() {

        LinearLayout viewLL, editLL, deleteLL;

        BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        View view = mInflator.inflate(R.layout.pedido_dialog_bottom_sheet, null);
        viewLL = view.findViewById(R.id.view_ll_bottom_sheet);
        editLL = view.findViewById(R.id.edit_ll_bottom_sheet);
        deleteLL = view.findViewById(R.id.delete_ll_bottom_sheet);

        dialog.setContentView(view);

        viewLL.setOnClickListener(this);
        editLL.setOnClickListener(this);
        deleteLL.setOnClickListener(this);

        dialog.show();
    }

    private void visualizarPedido(Pedido pedido) {


        Intent intent = new Intent(mContext, VisualizarPedidoActivity.class);
        Bundle params = new Bundle();
        params.putLong("keyPedido", pedido.getId());
        intent.putExtras(params);
        mContext.getApplicationContext().startActivity(intent);

    }

    private void editarPedido(final Pedido pedidoSelecionado) {
        if(!pedidoSelecionado.isCancelado()) {
            Intent intent = new Intent(mContext, VendasActivity.class);
            Bundle params = new Bundle();
            params.putSerializable("keyCliente", new Cliente());
            params.putLong("keyPedido", pedidoSelecionado.getId());
            intent.putExtras(params);
            mContext.startActivity(intent);
        }else{
            Toast.makeText(mContext,"PEDIDO JÁ FOI CANCELADO!",Toast.LENGTH_LONG).show();
        }
    }

    private void deletarPedido(final Pedido pedidoSelecionado) {
       if(!pedidoSelecionado.isCancelado()){
       cancelamentoDialog(pedidoSelecionado);
       }else{
           Toast.makeText(mContext,"PEDIDO JÁ FOI CANCELADO!",Toast.LENGTH_LONG).show();
       }
    }

    @Override
    public ClientePedidoViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View movieCategoryView = mInflator.inflate(R.layout.header_layout, parentViewGroup, false);
        return new ClientePedidoViewHolder(movieCategoryView);
    }

    @Override
    public PedidosViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View moviesView = mInflator.inflate(R.layout.child_layout, childViewGroup, false);
        return new PedidosViewHolder(moviesView);
    }


    @Override
    public void onBindParentViewHolder(ClientePedidoViewHolder clientePedidoViewHolder, int position,
            ParentListItem parentListItem) {
        ClientePedido clientePedido = (ClientePedido) parentListItem;
        clientePedidoViewHolder.bind(clientePedido.getCliente());

    }

    @Override
    public void onBindChildViewHolder(PedidosViewHolder pedidosViewHolder, int position, Object childListItem) {
        Pedido pedido = (Pedido) childListItem;
        pedidosViewHolder.bind(pedido);
        pedidosViewHolder.setClickSubItemListener(this);


    }

    @Override
    public void notifyChildItemChanged(final int parentPosition, final int childPosition) {
        super.notifyChildItemChanged(parentPosition, childPosition);
    }

    private void cancelamentoDialog(final Pedido pedido) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.cancela_pedido_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("CANCELAMENTO DO PEDIDO");

        final TextView motivoCancelamentoETextView;
        Button simButton,naoButton;

        motivoCancelamentoETextView = dialogView.findViewById(R.id.motivo_cancelamento_edt);
        simButton = dialogView.findViewById(R.id.sim_button);
        naoButton = dialogView.findViewById(R.id.nao_button);




        final AlertDialog b = dialogBuilder.create();
        b.show();

        naoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        simButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama o edit no item pedido dao e chama edit na lista de itens


                if(!motivoCancelamentoETextView.getText().toString().isEmpty() ){

                    PedidoDAO pedidoDAO = PedidoDAO.getInstace();

                    pedido.setCancelado(true);
                    pedido.setMotivoCancelamento(motivoCancelamentoETextView.getText().toString());
                    pedidoDAO.updatePedido(pedido);


                    b.dismiss();
                    Toast.makeText(mContext, "PEDIDO CANCELADO COM SUCESSO", Toast.LENGTH_LONG).show();

                }
                else{ motivoCancelamentoETextView.setError("MOTIVO DO CANCELAMENTO É OBRIGATORIO!");

                }

            }
        });
        b.show();

    }



}