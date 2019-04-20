package com.br.minasfrango.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.ClientePedido;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.ui.adapter.ClientePedidoAdapter;
import com.br.minasfrango.ui.adapter.ExpandableRecyclerAdapter;
import com.br.minasfrango.ui.mvp.pedido.IPedidoActivityPresenter;
import com.br.minasfrango.ui.mvp.pedido.IPedidoActivityView;
import com.br.minasfrango.ui.mvp.pedido.PedidoActivityPresenter;
import java.util.ArrayList;
import java.util.List;


public class PedidoActivity extends AppCompatActivity implements IPedidoActivityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PedidoDAO mPedidoDAO;

    private ClientDAO mClientDAO;

    private ClientePedidoAdapter mAdapter;

    private RecyclerView recyclerView;

    List<ClientePedido> mClientePedidos;

    private IPedidoActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        presenter = new PedidoActivityPresenter(this);
        ButterKnife.bind(this);
        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mPedidoDAO = PedidoDAO.getInstace(Pedido.class);
        mClientDAO = ClientDAO.getInstace(Cliente.class);

        fillAdapter();

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

    protected void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);

    }

    protected void fillAdapter() {
        mClientePedidos = new ArrayList<>();
        List<Pedido> auxPedidos = mPedidoDAO.findAll();
        List<Cliente> clientes = mClientDAO.findClientByOrder(auxPedidos);
        List<Pedido> pedidos = new ArrayList<Pedido>();
        for (Cliente cliente : clientes) {
            pedidos = mPedidoDAO.pesquisarPedidosPorCliente(cliente);
            if (pedidos.size() > 0) {
                ClientePedido clientePedido = new ClientePedido(cliente, pedidos);
                mClientePedidos.add(clientePedido);
            }

            pedidos.clear();

        }

        mAdapter = new ClientePedidoAdapter(this, mClientePedidos);

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

    private void initViews() {
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupRecyclerView();

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
