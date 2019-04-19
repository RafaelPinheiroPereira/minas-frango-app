package com.br.minasfrango.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.listener.IDrawer;
import com.br.minasfrango.listener.NavigateDrawer;
import com.br.minasfrango.ui.abstracts.AbstractActivity;
import com.br.minasfrango.ui.adapter.ClientePedidoAdapter;
import com.br.minasfrango.ui.adapter.ExpandableRecyclerAdapter;
import com.br.minasfrango.data.dao.ClientDAO;
import com.br.minasfrango.data.dao.PedidoDAO;
import com.br.minasfrango.data.pojo.Cliente;
import com.br.minasfrango.data.pojo.ClientePedido;
import com.br.minasfrango.data.pojo.Pedido;
import com.br.minasfrango.ui.mvp.pedido.IPedidoActivityPresenter;
import com.br.minasfrango.ui.mvp.pedido.IPedidoActivityView;
import com.br.minasfrango.ui.mvp.pedido.PedidoActivityPresenter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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


    private Drawer result;
    private IDrawer navigateDrawer;
    private IPedidoActivityPresenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        presenter= new PedidoActivityPresenter(this);
        ButterKnife.bind(this);
        initViews();
        presenter.setDrawer(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPedidoDAO = PedidoDAO.getInstace(Pedido.class);
        mClientDAO = ClientDAO.getInstace(Cliente.class);

        fillAdapter();

    }




    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void setDrawer(final Bundle savedInstanceState) {
        navigateDrawer = new NavigateDrawer(this);
        result = navigateDrawer.builder(this, toolbar, savedInstanceState,presenter.getUserName());
        result.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position,
                    IDrawerItem drawerItem) {
                switch (position) {
                    case 1:
                        AbstractActivity.navigateToActivity(presenter.getContext(),new Intent(presenter.getContext(), HomeActivity.class));
                        break;
                    case 2:
                        AbstractActivity.navigateToActivity(presenter.getContext(),new Intent(presenter.getContext(), PedidoActivity.class));
                        break;
                    case 3:
                        break;
                    case 4:
                        AbstractActivity.navigateToActivity(presenter.getContext(),
                                new Intent(presenter.getContext(), SincronizarDadosActivity.class));
                        break;

                    case 5:
                        confirmAlertDialog();
                        break;
                }
                return true;
            }
        });

        navigateDrawer.addItemInDrawer(result);



    }

    @Override
    public void confirmAlertDialog() {
        AlertDialog.Builder builder = new Builder(presenter.getContext());
        builder.setTitle("Realizar Logout");
        builder.setMessage("Deseja realmente sair do sistema?");

        String positiveText = "Sim";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        presenter.logout();
                        dialog.dismiss();
                        //edtConfirmaEmail.setText("");

                        return;
                    }
                });

        String negativeText = "Não";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
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
        List<Cliente> clientes = mClientDAO.findClientByOrder(auxPedidos);
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
