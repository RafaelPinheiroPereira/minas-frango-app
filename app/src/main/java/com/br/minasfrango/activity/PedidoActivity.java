package com.br.minasfrango.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.adapter.ClientePedidoAdapter;
import com.br.minasfrango.data.adapter.ExpandableRecyclerAdapter;
import com.br.minasfrango.data.dao.ClienteDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.ClientePedido;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.util.SessionManager;
import java.util.ArrayList;
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
        recyclerView = findViewById(R.id.recyclerview);

    }


    protected void fillAdapter() {
        mClientePedidos= new ArrayList<>();
        List<Pedido> auxPedidos = mPedidoDAO.findAll();
        List<Cliente> clientes = mClienteDAO.pesquisarClientePorPedido(auxPedidos);
        List<Pedido> pedidos= new ArrayList<Pedido>();
        for(Cliente cliente:clientes){
            pedidos= mPedidoDAO.pesquisarPedidosPorCliente(cliente);
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


            }

            @Override
            public void onListItemCollapsed(int position) {



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
