package com.br.minasfrango.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.br.minasfrango.R;
import com.br.minasfrango.data.model.Pedido;
import com.br.minasfrango.data.realm.ClientePedido;
import com.br.minasfrango.ui.adapter.ExpandableRecyclerAdapter;
import com.br.minasfrango.ui.adapter.PedidosAdapter;
import com.br.minasfrango.ui.mvp.pedido.IPedidoMVP;
import com.br.minasfrango.ui.mvp.pedido.Presenter;
import com.br.minasfrango.util.AlertDialogOrderCancel;
import com.br.minasfrango.util.BottomSheet;
import java.util.List;

public class PedidoActivity extends AppCompatActivity implements IPedidoMVP.IView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.txt_quantidade_pedido)
    TextView txtQuantidadePedidos;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PedidosAdapter mAdapter;

    private IPedidoMVP.IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        ButterKnife.bind(this);
        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new Presenter(this);
        fillAdapter();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    public void dismiss() {
        presenter.getDialog().dismiss();
        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onShowBototoSheetDialog() {

        BottomSheet bottomSheet = new BottomSheet(this.presenter);
        bottomSheet.createBottomSheet();

    }

    @Override
    public void showDialogCanceling(final Pedido pedido) {

        AlertDialogOrderCancel alertDialogOrderCancel = new AlertDialogOrderCancel(presenter);
        AlertDialog alertDialog = alertDialogOrderCancel.builder(pedido);
        alertDialog.setTitle("Cancelar Pedido");
        alertDialog.show();
        this.presenter.setDialog(alertDialog);


    }

    protected void fillAdapter() {

        List<ClientePedido> clientePedidoList=presenter.obterTodosClientePedido();
        int count=0;
        for(ClientePedido clientePedido:clientePedidoList){
            count+=clientePedido.getChildItemList().size();
        }


        mAdapter = new PedidosAdapter(presenter,clientePedidoList );
        txtQuantidadePedidos.setText("N° de Pedidos:"+ count);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemCollapsed(int position) {

            }

            @Override
            public void onListItemExpanded(int position) {

            }
        });

    }

    private void initViews() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
