package com.br.minasfrango.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.br.minasfrango.R;
import com.br.minasfrango.adapter.ItemPedidoAdapter;
import com.br.minasfrango.adapter.ItensPedidoVisualizarAdapter;
import com.br.minasfrango.dao.ClienteDAO;
import com.br.minasfrango.dao.ItemPedidoDAO;
import com.br.minasfrango.dao.PedidoDAO;
import com.br.minasfrango.dao.PrecoDAO;
import com.br.minasfrango.dao.TipoRecebimentoDAO;
import com.br.minasfrango.dao.UnidadeDAO;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.model.Produto;
import com.br.minasfrango.model.TipoRecebimento;
import com.br.minasfrango.util.CurrencyEditText;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VisualizarPedidoActivity extends AppCompatActivity {
    
    PedidoDAO mPedidoDAO;
    ClienteDAO mClienteDAO;
    TipoRecebimentoDAO mTipoRecebimentoDAO;
    ItemPedidoDAO mItemPedidoDAO;

    private Toolbar mToolbar;
    RecyclerView itensRecyclerView;
    TextView nomeCienteTextView,enderecoTextView,numeroPedidoTextView,dataPedidoTextView,formaPagamentoTextView;


    ItensPedidoVisualizarAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pedido);
        
        initView();

        mPedidoDAO=PedidoDAO.getInstace();
        mClienteDAO=ClienteDAO.getInstace();
        mTipoRecebimentoDAO=TipoRecebimentoDAO.getInstace();
        mItemPedidoDAO=ItemPedidoDAO.getInstace();



    }

    @Override
    protected void onStart() {
        super.onStart();

        Pedido pedido= getParams();

        Cliente cliente=mClienteDAO.findById(pedido.getCodigoCliente());
        TipoRecebimento tipoRecebimento=mTipoRecebimentoDAO.findById(pedido.getTipoRecebimento());

        List<ItemPedido> itemPedidos =mItemPedidoDAO.allItensByPedido(pedido);

        mAdapter= new ItensPedidoVisualizarAdapter(this,itemPedidos);

        itensRecyclerView.setAdapter(mAdapter);
        itensRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        formaPagamentoTextView.setText(tipoRecebimento.getNome());
        dataPedidoTextView.setText(DateFormat.getDateInstance().format(pedido.getDataPedido()).toUpperCase());
        numeroPedidoTextView.setText(String.valueOf(pedido.getId()));
        nomeCienteTextView.setText(cliente.getNome());
        enderecoTextView.setText(cliente.getEndereco());









    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_search:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("VISUALIZAR PEDIDOS");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeCienteTextView=findViewById(R.id.fantasia_txt_visualizar_pedido);
        enderecoTextView=findViewById(R.id.endereco_txt_visualizar_pedido);
        numeroPedidoTextView=findViewById(R.id.num_pedido_visualiza_pedido);
        dataPedidoTextView=findViewById(R.id.data_visualiza_pedido);
        formaPagamentoTextView=findViewById(R.id.forma_pagamento_visualiza_pedido);

        itensRecyclerView=findViewById(R.id.recycleview_itensPedido);
    }


    private Pedido getParams() {
        Bundle args = getIntent().getExtras();
        long id = (long) args.getLong("keyPedido");

        return mPedidoDAO.findById(id);
    }


}
