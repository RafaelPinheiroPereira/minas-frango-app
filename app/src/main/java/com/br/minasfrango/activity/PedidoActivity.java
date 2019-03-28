package com.br.minasfrango.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.br.minasfrango.R;
import com.br.minasfrango.adapter.ClientePedidoAdapter;
import com.br.minasfrango.adapter.ExpandableRecyclerAdapter;
import com.br.minasfrango.dao.ClienteDAO;
import com.br.minasfrango.dao.PedidoDAO;
import com.br.minasfrango.listener.ClickSubItemListener;
import com.br.minasfrango.model.Cliente;
import com.br.minasfrango.model.ClientePedido;
import com.br.minasfrango.model.ItemPedido;
import com.br.minasfrango.model.Pedido;
import com.br.minasfrango.util.SessionManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PedidoActivity extends BaseActivity  {


    private PedidoDAO mPedidoDAO;

    private ClienteDAO mClienteDAO;

    private ClientePedidoAdapter mAdapter;
    private RecyclerView recyclerView;
    List<ClientePedido> mClientePedidos;


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        session = new SessionManager(getApplicationContext());
        if (session.checkLogin()) {
            finish();
        }

        setDrawer(savedInstanceState, R.layout.activity_pedido, "PEDIDOS");
        initDrawer(savedInstanceState);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPedidoDAO = PedidoDAO.getInstace();
        mClienteDAO = ClienteDAO.getInstace();

        fillAdapter();

    }




    private void initViews() {
        setupRecyclerView();

    }

    protected void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

    }


    protected void fillAdapter() {
        mClientePedidos= new ArrayList<>();
        List<Pedido> auxPedidos = mPedidoDAO.findAll();
        List<Cliente> clientes = mClienteDAO.carregaClienteByPedidos(auxPedidos);
        List<Pedido> pedidos= new ArrayList<Pedido>();
        for(Cliente cliente:clientes){
            pedidos= mPedidoDAO.buscaPedidosPorCliente(cliente);
            if(pedidos.size()>0) {
                ClientePedido clientePedido = new ClientePedido(cliente, pedidos);
                mClientePedidos.add(clientePedido);
            }

            pedidos.clear();

        }

        mAdapter= new ClientePedidoAdapter(this,mClientePedidos);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
                ClientePedido clientePedido = mClientePedidos.get(position);

                String toastMsg = clientePedido.getCliente().getNome();
                Toast.makeText(PedidoActivity.this,
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onListItemCollapsed(int position) {
                ClientePedido collapsedClientePedido = mClientePedidos.get(position);

                String toastMsg =collapsedClientePedido.getCliente().getNome() ;
                Toast.makeText(PedidoActivity.this,
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });





    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.onRestoreInstanceState(savedInstanceState);
    }


}
