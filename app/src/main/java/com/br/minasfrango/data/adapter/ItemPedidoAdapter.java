package com.br.minasfrango.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.PrecoDAO;
import com.br.minasfrango.data.dao.UnidadeDAO;
import com.br.minasfrango.data.pojo.ItemPedido;
import com.br.minasfrango.data.pojo.Preco;
import com.br.minasfrango.data.pojo.Produto;
import com.br.minasfrango.listener.RecyclerViewOnClickListenerHack;
import com.br.minasfrango.util.CurrencyEditText;
import com.br.minasfrango.util.FormatacaoMoeda;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 04717299302 on 13/01/2017.
 */

public class ItemPedidoAdapter extends RecyclerView.Adapter<ItemPedidoAdapter.MyViewHolder>
        implements View.OnClickListener, Filterable {

    private Context mContext;

    private List<ItemPedido> itemPedidoListFiltered;

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public TextView txtDescricao, idTextView, txtUnidade, txtQuantidade, txtValorTotal;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtDescricao = itemView.findViewById(R.id.textViewDescricaoItem);
            idTextView = itemView.findViewById(R.id.textViewID);
            txtUnidade = itemView.findViewById(R.id.textViewUnidade);
            txtQuantidade = itemView.findViewById(R.id.textViewQTDItem);
            txtValorTotal = itemView.findViewById(R.id.textValorTotal);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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


    private LayoutInflater mLayoutInflater;

    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    ItemPedidoDAO itemPedidoDAO;

    TextView txtValorTotal;

    UnidadeDAO unidadeDAO;

    private ArrayAdapter<String> adapterUnidade;

    String codigoUnidade;

    private Preco preco;

    PrecoDAO precoDAO;

    Double valorTotal;

    int posicao;

    private List<ItemPedido> mList;

    public ItemPedidoAdapter(Context c, List<ItemPedido> l, TextView valorTotal) {
        this.mContext = c;
        this.itemPedidoListFiltered = l;
        this.mList = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemPedidoDAO = ItemPedidoDAO.getInstace();
        txtValorTotal = valorTotal;


    }

    @Override
    public void onBindViewHolder(final ItemPedidoAdapter.MyViewHolder myViewHolder, int position) {
        final ItemPedido itemPedido = itemPedidoListFiltered.get(position);
        posicao = position;

        myViewHolder.idTextView.setText(String.valueOf(itemPedido.getChavesItemPedido().getIdProduto()));
        myViewHolder.txtDescricao.setText((itemPedido.getDescricao()));
        myViewHolder.txtUnidade.setText(itemPedido.getChavesItemPedido().getIdUnidade());
        myViewHolder.txtQuantidade.setText(String.valueOf(itemPedido.getQuantidade()));
        myViewHolder.txtValorTotal
                .setText(FormatacaoMoeda.convertDoubleToString(itemPedido.getValorTotal()));

    }

    @Override
    public int getItemCount() {
        return itemPedidoListFiltered.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    public void addListItem(ItemPedido c, int position) {
        mList.add(c);
        notifyItemInserted(position);
    }

    public void updateListItem(ItemPedido c, int position) {
        mList.set(position, c);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemPedidoListFiltered = mList;
                } else {
                    List<ItemPedido> filteredList = new ArrayList<>();
                    for (ItemPedido item : mList) {

                        if (item.getDescricao().toLowerCase().contains(charString.toLowerCase()) || String
                                .valueOf(item.getId()).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }

                    itemPedidoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemPedidoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemPedidoListFiltered = (ArrayList<ItemPedido>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public ItemPedidoAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_pedido, viewGroup, false);
        ItemPedidoAdapter.MyViewHolder mvh = new ItemPedidoAdapter.MyViewHolder(v);
        return mvh;


    }

    private double calculaValorTotalPedido(List<ItemPedido> itens) {
        Double valorTotalPedido = 0.0;
        for (int i = 0; i < itens.size(); i++) {
            valorTotalPedido += itens.get(i).getValorTotal();
        }
        return valorTotalPedido;
    }

    private void initDialog(final ItemPedido itemPedido, final int position, final TextView txtValorTotal,
            final TextView totalizador) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_edit_item_pedido, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("DADOS DO ITEM");

        TextView idProdutoTextView, descricaoProdutoTextView;
        final EditText quantidadeEditText;
        final CurrencyEditText precoEditText;
        Spinner unidadeSpinner = null;
        Button cancelarEdicaoButton, salvaEdicaoButton;
        ArrayList<String> unidades = new ArrayList<String>();
        ArrayList<String> todasUnidades = new ArrayList<String>();
        String unidadePadrao = "";

        idProdutoTextView = dialogView.findViewById(R.id.txt_codigo_produto);
        descricaoProdutoTextView = dialogView.findViewById(R.id.txt_descricao_produto);
        quantidadeEditText = dialogView.findViewById(R.id.edt_quantidade_produto);
        precoEditText = dialogView.findViewById(R.id.edt_preco);
        unidadeSpinner = dialogView.findViewById(R.id.spinner_unidade_produto);
        cancelarEdicaoButton = dialogView.findViewById(R.id.button_cancelar_edicao);
        salvaEdicaoButton = dialogView.findViewById(R.id.button_confirmar_edicao);

        idProdutoTextView.setText(String.valueOf(itemPedido.getChavesItemPedido().getIdProduto()));
        descricaoProdutoTextView.setText(itemPedido.getDescricao());
        precoEditText.setText(String.valueOf(itemPedido.getValorUnitario()));
        quantidadeEditText.setText(String.valueOf(itemPedido.getQuantidade()));

        unidadeDAO = UnidadeDAO.getInstace();
        precoDAO = PrecoDAO.getInstace();
        final Produto produto = new Produto();
        produto.setId((long) itemPedido.getChavesItemPedido().getIdProduto());
        produto.setNome(itemPedido.getDescricao());
        unidadePadrao = unidadeDAO.carregaUnidadePadraoProduto(produto);
        unidades.add(unidadePadrao);
        todasUnidades = unidadeDAO.carregaUnidadesProduto(produto);
        unidades.addAll(todasUnidades);
        adapterUnidade = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, unidades);
        //adapter da unidade
        unidadeSpinner.setAdapter(adapterUnidade);
        codigoUnidade = adapterUnidade.getItem(0);

        unidadeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                codigoUnidade = (String) parent.getAdapter().getItem(position);
                preco = precoDAO.carregaPrecoUnidadeProduto(produto, codigoUnidade);
                precoEditText.setText(String.valueOf(preco.getValor()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();

        cancelarEdicaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        salvaEdicaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama o edit no item pedido dao e chama edit na lista de itens

                ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstace();

                itemPedido.setQuantidade(Integer.parseInt(quantidadeEditText.getText().toString()));
                itemPedido.setValorUnitario(precoEditText.getCurrencyDouble());

                ItemPedidoAdapter.this.valorTotal = 0.0;
                int quantidadeAtual = Integer.parseInt(quantidadeEditText.getText().toString());
                if (quantidadeAtual >= 0) {
                    Double preco = precoEditText.getCurrencyDouble();
                    ItemPedidoAdapter.this.valorTotal = (quantidadeAtual * preco);
                    txtValorTotal
                            .setText(NumberFormat.getCurrencyInstance().format(ItemPedidoAdapter.this.valorTotal));

                }

                itemPedido.setValorTotal(ItemPedidoAdapter.this.valorTotal);
                itemPedido.setDescricao(produto.getNome());
                itemPedido.getChavesItemPedido().setIdUnidade(codigoUnidade);
                itemPedidoDAO.updateItem(itemPedido);

                updateListItem(itemPedido, position);
                totalizador.setText(
                        "Totalizador" + NumberFormat.getCurrencyInstance(new Locale("pt", "BR"))
                                .format(calculaValorTotalPedido(mList)));

                b.dismiss();
                Toast.makeText(mContext, "Item alterado com sucesso", Toast.LENGTH_LONG).show();
            }
        });
        b.show();

    }


}
