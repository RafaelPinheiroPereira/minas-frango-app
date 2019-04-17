package com.br.minasfrango.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.br.minasfrango.R;
import com.br.minasfrango.data.adapter.ClientePedidoAdapter;
import com.br.minasfrango.data.adapter.ExpandableRecyclerAdapter;
import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.ClientePedido;
import com.br.minasfrango.data.pojo.Pedido;
import com.mikepenz.materialdrawer.Drawer;
import java.util.ArrayList;
import java.util.List;


public class PedidoActivity extends AppCompatActivity {


    private PedidoDAO mPedidoDAO;

    private ClientDAO mClientDAO;

    private ClientePedidoAdapter mAdapter;
    private RecyclerView recyclerView;
    List<ClientePedido> mClientePedidos;


    private Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPedidoDAO = PedidoDAO.getInstace();
        mClientDAO = ClientDAO.getInstace();

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
        List<Cliente> clientes = mClientDAO.pesquisarClientePorPedido(auxPedidos);
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
