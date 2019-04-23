package com.br.minasfrango.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.ItemPedidoDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.ItemPedido;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.data.pojo.TipoRecebimento;
import com.br.minasfrango.ui.adapter.ItensPedidoVisualizarAdapter;
import java.text.DateFormat;
import java.util.List;

public class VisualizarPedidoActivity extends AppCompatActivity {
    
    PedidoDAO mPedidoDAO;

    ClientDAO mClientDAO;
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

        mPedidoDAO= PedidoDAO.getInstace(Pedido.class);
        mClientDAO = ClientDAO.getInstace(Cliente.class);
        mTipoRecebimentoDAO=TipoRecebimentoDAO.getInstace();
        mItemPedidoDAO= ItemPedidoDAO.getInstace(ItemPedido.class);



    }

    @Override
    protected void onStart() {
        super.onStart();

        Pedido pedido= getParams();

        Cliente cliente = mClientDAO.findById(pedido.getCodigoCliente());
        TipoRecebimento tipoRecebimento= null;
        try {
            tipoRecebimento = mTipoRecebimentoDAO.findById(pedido.getTipoRecebimento());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

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
        long id = args.getLong("keyPedido");

        return mPedidoDAO.findById(id);
    }


}
